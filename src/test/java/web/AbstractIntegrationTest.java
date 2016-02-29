package web;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class AbstractIntegrationTest {

	public static Vertx vertx;

	@BeforeClass
	public static void setUp(TestContext context) {
		vertx = Vertx.vertx();
		vertx.deployVerticle(MainVerticle.class.getName(), context.asyncAssertSuccess());
	}

	@AfterClass
	public static void tearDown(TestContext context) {
		vertx.close(context.asyncAssertSuccess());
	}

}
