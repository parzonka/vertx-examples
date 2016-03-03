package web;

import static web.ResponseUtil.respondWithCreated;
import static web.ResponseUtil.respondWithDeleted;
import static web.ResponseUtil.respondWithJson;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;

public class MessageController {

	private final Router router;

	final MessageService messageService;

	public MessageController(Vertx vertx) {
		this.router = Router.router(vertx);
		this.messageService = new MessageService("foo", "bar", "baz");
		configure();
	}

	protected void configure() {

		// return a list of messages
		router.route(HttpMethod.GET, "/messages").handler(respondWithJson(request -> messageService.getMessages()));

		//
		router.route(HttpMethod.POST, "/messages").handler(rc -> {
			final String content = rc.getBodyAsString();
			final Message message = messageService.store(content);
			respondWithCreated(rc, "" + message.getId(), message);
		});

		// deletes a message
		router.route(HttpMethod.DELETE, "/messages/:id").handler(routingContext -> {
			final long id = Long.parseLong(routingContext.request().getParam("id"));
			messageService.deleteMessage(id);
			respondWithDeleted(routingContext);
		});

	}

	public Router getRouter() {
		return router;
	}

}
