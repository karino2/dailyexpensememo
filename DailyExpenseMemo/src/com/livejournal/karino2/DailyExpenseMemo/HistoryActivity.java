package com.livejournal.karino2.DailyExpenseMemo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class HistoryActivity extends ListActivity {
	Database database;
	
	Hashtable<Long, String> categoryMap;
	Hashtable<Long, String> bookMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		database = new Database();
		database.open(this);
		
		
		long bookId = DailyExpenseMemoActivity.getBookId(this);
		
		Cursor cursor = database.fetchAllEntry(bookId);
		startManagingCursor(cursor);
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.history_item, 
				cursor, new String[] {"DATE", "category.NAME", "MEMO", "PRICE", "BUSINESS"}, 
				new int[] {R.id.dateTextView, R.id.categoryTextView, R.id.memoTextView, R.id.priceTextView, R.id.businessView});
		adapter.setViewBinder(new ViewBinder() {

			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				if(columnIndex == 2) {
					TextView tv = (TextView)view;
					SimpleDateFormat  sdf = new SimpleDateFormat("yyyy/MM/dd");
					tv.setText(sdf.format(new Date(cursor.getLong(columnIndex))));
		            return true;
				}
				if(columnIndex == 3) {
					TextView tv = (TextView)view;
					tv.setText(categoryMap.get(cursor.getLong(columnIndex)));
					return true;
				}
				if(columnIndex == 6) {
					TextView tv = (TextView)view;
					if(cursor.getInt(columnIndex) == 1)
						tv.setText("v");
					else
						tv.setText("");
					return true;
				}
				return false;
			}});
		setListAdapter(adapter);
		super.onCreate(savedInstanceState);
	}
	
}
