package http;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

public class SecurityConfig {

	public static Handler<RoutingContext> addSecurityHeaders() {
		return (RoutingContext rc) -> {
			final HttpServerResponse response = rc.response();
			// prevent caching for HTTP/1.1
			response.putHeader("Cache-Control", "no-store, no-cache, must-revalidate");
			// prevent caching for HTTP/1.0
			response.putHeader("Pragma", "no-cache").putHeader("Expires", "0");
			// prevents Internet Explorer from MIME - sniffing a
			// response away from the declared content-type
			response.putHeader("X-Content-Type-Options", "nosniff");
			// Strict HTTPS (for about ~6Months)
			response.putHeader("Strict-Transport-Security", "max-age=15768000 ; includeSubDomains");
			// IE8+ do not allow opening of attachments in the context
			// of this resource
			response.putHeader("X-Download-Options", "noopen");
			// enable XSS protection for IE
			response.putHeader("X-XSS-Protection", "1; mode=block");
			// deny frames
			response.putHeader("X-FRAME-OPTIONS", "DENY");

			rc.next();
		};
	}
}
