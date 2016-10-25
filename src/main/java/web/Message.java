package web;

import lombok.Value;

@Value
public class Message implements Comparable<Message> {

	private final long id;
	private final String content;

	@Override
	public int compareTo(Message o) {
		return Long.compare(getId(), o.getId());
	}
}
