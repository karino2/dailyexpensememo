package com.livejournal.karino2.DailyExpenseMemo.test;

import java.util.Date;
import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;

import com.livejournal.karino2.DailyExpenseMemo.CsvImporter;
import com.livejournal.karino2.DailyExpenseMemo.Entry;
import com.livejournal.karino2.DailyExpenseMemo.EntryActivity;
import com.livejournal.karino2.DailyExpenseMemo.EntryStorable;
import com.livejournal.karino2.DailyExpenseMemo.EntryStore;

import static org.junit.Assert.*;

public class CsvImporterTest {
	
	long returnId;
	String argCategory;
	
	@Before
	public void setUp()
	{
		returnId = -1;
		argCategory = null;
	}
	
	
	EntryStorable createStorableMock()
	{
		return new EntryStorable() {
			
			@Override
			public long toId(String category) {
				argCategory = category;
				return returnId;
			}
			
			@Override
			public void save(Entry ent) {
				// do nothing
			}
		};
	}
	
	static final long BOOK_ID = 5; // whatever.
	
	@Test
	public void test_readLine_newCategory() {
		long categoryId = 9; 
		returnId = categoryId;
		
		CsvImporter importer = new CsvImporter(BOOK_ID, createStorableMock());
		Entry ent = importer.parseLine("2011/11/08,Foods,-105.00,うどん");
		assertEqualsDate(2011, 11, 8, ent.getDate());
		assertEquals(categoryId, ent.getCategoryId());
		assertEquals(105, ent.getPrice());
		assertEquals("うどん", ent.getMemo());		
	}
	
	@Test
	public void test_readLine_noMemo() {
		long categoryId = 9; 
		returnId = categoryId;
		
		CsvImporter importer = new CsvImporter(BOOK_ID, createStorableMock());
		Entry ent = importer.parseLine("2011/11/08,Foods,-105.00,");
		assertEqualsDate(2011, 11, 8, ent.getDate());
		assertEquals(categoryId, ent.getCategoryId());
		assertEquals(105, ent.getPrice());
	}
	
	@Test
	public void test_readLine_price_int() {
		long categoryId = 9; 
		returnId = categoryId;
		
		CsvImporter importer = new CsvImporter(BOOK_ID, createStorableMock());
		Entry ent = importer.parseLine("2011/11/08,Foods,-105,うどん");
		assertEquals(105, ent.getPrice());
		
	}
	
	// also basic test of EntryStore.
	@Test
	public void test_readLine_knownCategory_with_real_EntryStore() {
		long foodId = 4;
		
		Hashtable<Long, String> catMap = new Hashtable<Long, String>();
		catMap.put(3l, "Coffe");
		catMap.put(foodId, "Foods");

		EntryStore store = new EntryStore(null);
		store.setCategoryMap(catMap);
				
		CsvImporter importer = new CsvImporter(BOOK_ID, store);
		Entry ent = importer.parseLine("2011/11/08,Foods,-105.00,うどん");
		assertEquals(foodId, ent.getCategoryId());
	}
	
	void assertEqualsDate(int expectedYear, int expectedMonth, int expectedDay, Date actual)
	{
		Date expect = createDate(expectedYear, expectedMonth, expectedDay);
		assertEquals(expect, actual);
	}

	static Date createDate(int expectedYear, int expectedMonth, int expectedDay) {
		Date expect = new Date(expectedYear-1900, expectedMonth-1, expectedDay);
		return expect;
	}
	
	// EntryActivityTest
	@Test
	public void test_nextDate_endOfMonth()
	{
		Date dt = createDate(2011, 11, 30);
		Date next = EntryActivity.DateUtility.nextDate(dt);
		
		assertEqualsDate(2011,12,1, next);		
	}
	
	@Test
	public void test_prevDate_beginningOfMonth()
	{
		Date dt = createDate(2011, 11, 1);
		Date prev = EntryActivity.DateUtility.prevDate(dt);
		
		assertEqualsDate(2011,10,31, prev);		
	}
}
