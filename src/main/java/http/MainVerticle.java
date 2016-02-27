package http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;

public class MainVerticle extends AbstractVerticle {

	private static Handler<RoutingContext> html(String html) {
		return (RoutingContext rc) -> {
			HttpServerResponse response = rc.response();
			response.putHeader("content-type", "text/html").end(html);
		};
	}

	private static Handler<RoutingContext> json(Object... objects) {
		return (RoutingContext rc) -> rc.response().putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encode(objects));
	}

	@Override
	public void start() {

		Router router = Router.router(vertx);

		router.route("/").handler(html("<h1>Hello World!</h1>"));

		// return a single message
		router.route("/message").handler(json(new Message(42, "Some Content")));

		// return a list of messages
		router.route("/messages").handler(json(new Message(42, "Some Content"), new Message(43, "More Content")));

		// disabling caches allows updates in browser
		router.route("/static/*").handler(
				StaticHandler.create("static").setCachingEnabled(!Boolean.getBoolean("vertx.disableFileCaching")));

		vertx.createHttpServer().requestHandler(router::accept)
				.listen(Integer.getInteger("server.port", 8080), System.getProperty("server.host", "0.0.0.0"));
	}
}
