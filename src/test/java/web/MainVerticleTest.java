package web;

import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;

import org.junit.Test;

public class MainVerticleTest extends AbstractAsyncTest {

	@Test
	public void testMyApplication(TestContext context) {
		final Async async = context.async();
		vertx.createHttpClient().getNow(8080, "localhost", "/api/messages", response -> {
			response.handler(body -> {
				context.assertTrue(body.toString().contains("foo"));
				async.complete();
			});
		});
	}

}
