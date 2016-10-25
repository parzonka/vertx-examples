package web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import lombok.extern.java.Log;

@Log
public class MainVerticle extends AbstractVerticle {

	@Override
	public void start() {

		final Router router = Router.router(vertx);

		// secure only in production
		if (!Boolean.getBoolean("vertx.development")) {
			System.out.println("production mode");
			SecurityConfig.addSecurity(router, vertx);
		} else {
			System.out.println("development mode");
		}

		// store post bodies in rc for all api calls
		router.route(HttpMethod.POST, "/api/*").handler(BodyHandler.create());
		// mount sub routers
		router.mountSubRouter("/api/messages", new MessageController(vertx).getRouter());

		// register sockjs bridge to event bus
		router.route("/eventbus/*").handler(new SockJSBridge(vertx));

		// disable cache for static asset reload
		router.route("/*").handler(StaticHandler.create().setCachingEnabled(!Boolean.getBoolean("vertx.development")));

		// enable http compression (e.g. gzip js)
		final HttpServerOptions options = new HttpServerOptions().setCompressionSupported(true);

		// create server
		HttpServer httpServer = vertx.createHttpServer(options);
		httpServer.requestHandler(router::accept).listen(Integer.getInteger("server.port", 8080),
				System.getProperty("server.host", "0.0.0.0"));

	}
}
