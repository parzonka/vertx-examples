package web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import lombok.extern.java.Log;

@Log
public class MainVerticle extends AbstractVerticle {

	/**
	 * Ends response with a JSON list of objects
	 * 
	 * @param objects
	 * @return
	 */
	private static Handler<RoutingContext> toJson(Supplier<List<?>> listSupplier) {
		return (RoutingContext rc) -> {
			rc.response().putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encode(listSupplier.get()));
		};
	}

	private static <T> Handler<RoutingContext> createFromJson(Class<T> clazz, Consumer<T> consumer) {
		return (RoutingContext rc) -> {
			final T instance = Json.decodeValue(rc.getBodyAsString(), clazz);
			consumer.accept(instance);
			rc.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(instance));
		};
	}

	private static Handler<RoutingContext> createFromString(Consumer<String> consumer) {
		return (RoutingContext rc) -> {
			final String instance = rc.getBodyAsString();
			consumer.accept(instance);
			rc.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(instance));
		};
	}

	@Override
	public void start() {

		final MessageService messageService = new MessageService("foo", "bar", "baz");

		final Router router = Router.router(vertx);

		if (Boolean.getBoolean("vertx.development")) {
			log.info("Starting in development mode");
		}

		SecurityConfig.addSecurity(router, vertx);

		// store post bodies in rc
		router.route(HttpMethod.POST, "/api/*").handler(BodyHandler.create());
		router.route(HttpMethod.POST, "/api/messages").handler(
				createFromString(message -> messageService.store(message)));

		// return a list of messages
		router.route(HttpMethod.GET, "/api/messages").handler(toJson(() -> messageService.getMessages()));

		// return a list of messages
		router.route(HttpMethod.DELETE, "/api/messages/:id").handler(rc -> {
			final String id = rc.request().getParam("id");
			messageService.deleteMessage(Long.parseLong(id));
			rc.response().setStatusCode(204).end();
		});

		// disable cache for static asset reload
		router.route("/*").handler(StaticHandler.create().setCachingEnabled(!Boolean.getBoolean("vertx.development")));

		// enable http compression (e.g. gzip js)
		final HttpServerOptions options = new HttpServerOptions().setCompressionSupported(true);

		// create server
		vertx.createHttpServer(options).requestHandler(router::accept)
				.listen(Integer.getInteger("server.port", 8080), System.getProperty("server.host", "0.0.0.0"));
	}
}
