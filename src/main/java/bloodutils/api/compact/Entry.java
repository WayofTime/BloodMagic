package bloodutils.api.compact;

import bloodutils.api.entries.IEntry;

public class Entry {
	public Entry(IEntry[] entry, String name, int indexPage){
		this.entry = entry;
		this.name = name;
		this.indexPage = indexPage - 1;
	}
	public IEntry[] entry;
	public String name;
	public int indexPage;
}