package http;

import io.vertx.core.AbstractVerticle;

public class MainVerticle extends AbstractVerticle {

	@Override
	public void start() {
		vertx.createHttpServer().requestHandler(req -> {
			req.response().putHeader("content-type", "text/html").end("<h1>Hello World!</h1>");
		}).listen(8080);
	}
}
