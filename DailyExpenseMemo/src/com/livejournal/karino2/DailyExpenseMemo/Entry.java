package com.livejournal.karino2.DailyExpenseMemo;

import java.util.Date;

public class Entry {
	/*
import java.util.Hashtable;
	public static Hashtable<String, Integer> bookMap;
	public static Hashtable<String, Integer> categoryMap;
	*/
	public Entry(Date date, long category, String memo, long price, long book)
	{
		this(-1, date, category, memo, price, book);
	}	
	public Entry(long id, Date date, long category, String memo, long price, long book)
	{
		this.id = id;
		this.date = date;
		this.categoryId = category;
		this.memo = memo;
		this.price = price;
		this.bookId = book;		
	}
	
	public long getId() { return id; }
	
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getDate() {
		return date;
	}

	public void setCategoryId(long category) {
		this.categoryId = category;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMemo() {
		return memo;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public long getPrice() {
		return price;
	}

	public long getBookId() {
		return bookId;
	}

	private long id;
	private Date date;
	private long categoryId;
	private String memo;
	private long price;
	private long bookId;
}
