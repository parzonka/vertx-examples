package web;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.function.Function;

public class ResponseUtil {

	public static void respondWithCreated(RoutingContext routingContext, String location, Object content) {
		routingContext.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
				.putHeader("Location", routingContext.request().absoluteURI() + "/" + location).end(Json.encode(content));
	}

	public static void respondWithDeleted(RoutingContext routingContext) {
		routingContext.response().setStatusCode(204).putHeader("content-type", "application/json; charset=utf-8").end();
	}

	public static Handler<RoutingContext> respondWithJson(Function<HttpServerRequest, List<?>> listSupplier) {
		return (RoutingContext rc) -> {
			rc.response().putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encode(listSupplier.apply(rc.request())));
		};
	}

}
