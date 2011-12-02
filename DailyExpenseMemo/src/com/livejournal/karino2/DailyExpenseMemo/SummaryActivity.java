package com.livejournal.karino2.DailyExpenseMemo;

import java.text.DecimalFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SummaryActivity extends Activity {
	Database database;
	long bookId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		bookId = DailyExpenseMemoActivity.getBookId(this);
		database = new Database();
		database.open(this);
		
		setContentView(R.layout.summary);
		

		int total = totalPrice(1, 0);
		setText(R.id.today_total, String.valueOf(total));
		
		total = totalPrice(8, 1);
		setText(R.id.prev_7_days_avg, new DecimalFormat("0.##").format(total/7.0));
		setText(R.id.prev_7_days_total, String.valueOf(total));
		
		total = totalPrice(31, 1);
		setText(R.id.prev_30_days_avg, new DecimalFormat("0.##").format(total/30.0));
		setText(R.id.prev_30_days_total, String.valueOf(total));
		
		Date earliest = database.earliest(bookId);
		Date yesterday = beforeDate(new Date(), 1);
		total = database.totalPrice(bookId, earliest, yesterday);
		int between = Database.betweenDate(earliest, yesterday);
		
		setText(R.id.all_past_avg, new DecimalFormat("0.##").format(total/((double)between)));
		setText(R.id.all_past_total, String.valueOf(total));
	}
	int totalPrice(int fromDateBefore, int toDateBefore) {
		Date today = new Date();
		Date from = beforeDate(today,  fromDateBefore);
		Date to = beforeDate(today, toDateBefore);
		return database.totalPrice(bookId, from, to);
	}
	void setText(int id, String text) {
		TextView tv = (TextView)findViewById(id);
		tv.setText(text);
	}
	public static Date beforeDate(Date target, int minusDay) {
		return Database.beforeDate(target, minusDay);
	}
}
