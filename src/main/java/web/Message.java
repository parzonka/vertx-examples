package web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Comparable<Message> {

	private long id;
	private String content;

	@Override
	public int compareTo(Message o) {
		return Long.compare(getId(), o.getId());
	}
}
