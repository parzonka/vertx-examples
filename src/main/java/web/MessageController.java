package web;

import static web.ResponseUtil.respondWithCreated;
import static web.ResponseUtil.respondWithDeleted;
import static web.ResponseUtil.respondWithJson;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;

public class MessageController {

	private final Router router;

	private final MessageService messageService;

	public MessageController(Vertx vertx) {
		this.router = Router.router(vertx);
		this.messageService = new MessageService(vertx, "foo", "bar", "baz");
		configure();
	}

	protected void configure() {

		// returns all messages
		router.route(HttpMethod.GET, "/").handler(respondWithJson(request -> messageService.getMessages()));

		// stores a message
		router.route(HttpMethod.POST, "/").handler(routingContext -> {
			final String content = routingContext.getBodyAsString();
			final Message message = messageService.store(content);
			respondWithCreated(routingContext, "" + message.getId(), message);
		});

		// deletes a message
		router.route(HttpMethod.DELETE, "/:id").handler(routingContext -> {
			final long id = Long.parseLong(routingContext.request().getParam("id"));
			messageService.deleteMessage(id);
			respondWithDeleted(routingContext);
		});

	}

	public Router getRouter() {
		return router;
	}

}
