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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import lombok.extern.java.Log;

@Log
public class MainVerticle extends AbstractVerticle {

	private final List<Message> messages = new ArrayList<>(Arrays.asList(new Message(42, "foo"),
			new Message(43, "bar"), new Message(44, "baz")));

	/**
	 * Ends response with a JSON list of objects
	 * 
	 * @param objects
	 * @return
	 */
	private static Handler<RoutingContext> toJson(List<?> list) {
		return (RoutingContext rc) -> rc.response().putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encode(list));
	}

	private static <T> Handler<RoutingContext> createFromJson(Class<T> clazz, Consumer<T> consumer) {
		return (RoutingContext rc) -> {
			final T instance = Json.decodeValue(rc.getBodyAsString(), clazz);
			consumer.accept(instance);
			rc.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(instance));
		};
	}

	@Override
	public void start() {

		Router router = Router.router(vertx);

		if (Boolean.getBoolean("vertx.development")) {
			log.info("Starting in development mode");
		}

		SecurityConfig.addSecurity(router, vertx);

		// store post bodies in rc
		router.route(HttpMethod.POST, "/api/*").handler(BodyHandler.create());
		router.route(HttpMethod.POST, "/api/messages").handler(
				createFromJson(Message.class, message -> messages.add(message)));

		// return a list of messages
		router.route(HttpMethod.GET, "/api/messages").handler(toJson(messages));

		// disable cache for static asset reload
		router.route("/*").handler(StaticHandler.create().setCachingEnabled(!Boolean.getBoolean("vertx.development")));

		// enable http compression (e.g. gzip js)
		final HttpServerOptions options = new HttpServerOptions().setCompressionSupported(true);

		// create server
		vertx.createHttpServer(options).requestHandler(router::accept)
				.listen(Integer.getInteger("server.port", 8080), System.getProperty("server.host", "0.0.0.0"));
	}
}
