package data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DescriptionData {
	private final List<String> list;

	private DescriptionData(final List<String> list) {
		this.list = new ArrayList<>(list);
	}

	public String[] getDescriptionArray() {
		return (String[]) list.toArray();
	}

	public static class Builder {
		private final static String NO_DESCRIPTION = "no descriprion";
		private final List<String> list = new LinkedList<>();

		public Builder add(final String description) {
			if (description == null) {
				list.add(NO_DESCRIPTION);
			} else {
				list.add(description);
			}
			return this;
		}

		public DescriptionData build() {
			list.sort(null);
			return new DescriptionData(list);
		}
	}
}
