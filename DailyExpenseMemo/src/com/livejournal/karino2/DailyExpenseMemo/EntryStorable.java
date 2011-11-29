package com.livejournal.karino2.DailyExpenseMemo;

public interface EntryStorable {
	void save(Entry ent);
	long toId(String category);
}
