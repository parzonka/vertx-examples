package web;

import io.vertx.core.Vertx;
import io.vertx.core.json.Json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MessageService {

	private final AtomicLong counter;
	private final Map<Long, Message> messages;
	private final Vertx vertx;

	// TODO inject domain event service
	public MessageService(Vertx vertx, String... initialContent) {
		this.vertx = vertx;
		this.messages = new ConcurrentHashMap<Long, Message>();
		this.counter = new AtomicLong(0);
		for (String content : initialContent) {
			store(content);
		}
	}

	public Message store(String content) {
		final long id = counter.incrementAndGet();
		final Message message = new Message(id, content);
		messages.put(id, message);
		vertx.eventBus().publish("messages/created", Json.encode(message));
		return message;
	}

	public void deleteMessage(long id) {
		final Message message = messages.remove(id);
		vertx.eventBus().publish("messages/deleted", Json.encode(message));
	}

	public List<Message> getMessages() {
		final ArrayList<Message> result = new ArrayList<>(messages.values());
		Collections.sort(result);
		return result;
	}

}
