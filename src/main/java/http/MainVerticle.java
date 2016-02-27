package http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
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

	@Override
	public void start() {

		Router router = Router.router(vertx);

		router.route("/").handler(html("<h1>Hello World!</h1>"));

		// disabling caches allows updates in browser
		router.route("/static/*").handler(
				StaticHandler.create("static").setCachingEnabled(!Boolean.getBoolean("vertx.disableFileCaching")));

		vertx.createHttpServer().requestHandler(router::accept)
				.listen(Integer.getInteger("server.port", 8080), System.getProperty("server.host", "0.0.0.0"));
	}
}
