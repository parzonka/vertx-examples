package web;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

/**
 * Handles routing contexts bridging to the event bus via sockjs.
 *
 */
public class SockJSBridge implements Handler<RoutingContext> {

	private final Handler<RoutingContext> delegate;

	public SockJSBridge(Vertx vertx) {
		this.delegate = createDelegate(vertx);
	}

	private Handler<RoutingContext> createDelegate(Vertx vertx) {
		// permit all adresses; may be unsecure in a real application
		final BridgeOptions OPTIONS = new BridgeOptions().addOutboundPermitted(new PermittedOptions()
				.setAddressRegex(".*"));
		return SockJSHandler.create(vertx).bridge(OPTIONS, event -> {
			event.complete(true);
		});
	}

	@Override
	public void handle(RoutingContext event) {
		delegate.handle(event);
	}

}
