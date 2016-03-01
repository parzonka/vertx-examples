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

	public long store(String content) {
		final long id = counter.incrementAndGet();
		messages.put(id, new Message(id, content));
		return id;
	}

	public void deleteMessage(long id) {
		System.out.println("removing" + id);
		messages.remove(id);
	}

	public List<Message> getMessages() {
		final ArrayList<Message> result = new ArrayList<>(messages.values());
		Collections.sort(result);
		return result;
	}

}
