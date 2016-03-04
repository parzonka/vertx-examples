package web;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.sockjs.BridgeEventType;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Handles routing contexts bridging to the event bus via sockjs.
 *
 */
public class SockJSBridge implements Handler<RoutingContext> {

	private Vertx vertx;
	private final Handler<RoutingContext> delegate;
	private final AtomicInteger connections;

	public SockJSBridge(Vertx vertx) {
		this.vertx = vertx;
		this.delegate = createDelegate(vertx);
		this.connections = new AtomicInteger(0);
		vertx.setPeriodic(1000, id -> {
			publishCounts(connections.get());
		});
	}

	private void publishCounts(Integer count) {
		vertx.eventBus().publish("connections", "{\"count\": " + count + "}");
	}

	private Handler<RoutingContext> createDelegate(Vertx vertx) {
		// permit all adresses; may be unsecure in a real application
		final BridgeOptions OPTIONS = new BridgeOptions().addOutboundPermitted(new PermittedOptions()
				.setAddressRegex(".*"));

		return SockJSHandler.create(vertx).bridge(OPTIONS, event -> {

			if (event.type() == BridgeEventType.SOCKET_CREATED) {
				publishCounts(connections.incrementAndGet());
			}
			if (event.type() == BridgeEventType.SOCKET_CLOSED) {
				publishCounts(connections.decrementAndGet());
			}
			event.complete(true);
		});
	}

	@Override
	public void handle(RoutingContext event) {
		delegate.handle(event);
	}

}
