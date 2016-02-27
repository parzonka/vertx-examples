package http;

import io.vertx.core.Vertx;

public class Main {

	public static void main(final String... args) {
		final Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new Server());
	}

}
