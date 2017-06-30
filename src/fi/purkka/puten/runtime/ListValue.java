package fi.purkka.puten.runtime;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/** Represents a list of some values. */
public class ListValue implements Value {
	
	private final List<Value> list;
	
	public ListValue(List<Value> list) {
		this.list = list;
	}
	
	public static ListValue of(List<String> strings) {
		return new ListValue(strings.stream()
				.map(StrValue::new)
				.collect(Collectors.toList()));
	}
	
	@Override
	public String string() {
		return list.toString();
	}
	
	@Override
	public Iterator<Value> iterator() {
		return list.iterator();
	}
}