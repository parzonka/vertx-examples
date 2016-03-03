package web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MessageService {

	private final AtomicLong counter;
	private final Map<Long, Message> messages;

	public MessageService(String... initialContent) {
		this.messages = new ConcurrentHashMap<Long, Message>();
		this.counter = new AtomicLong(0);
		for (String content : initialContent) {
			store(content);
		}
	}

	public Message store(String content) {
		final long id = counter.incrementAndGet();
		Message message = new Message(id, content);
		messages.put(id, message);
		return message;
	}

	public void deleteMessage(long id) {
		messages.remove(id);
	}

	public List<Message> getMessages() {
		final ArrayList<Message> result = new ArrayList<>(messages.values());
		Collections.sort(result);
		return result;
	}

}
