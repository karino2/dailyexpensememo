package com.livejournal.karino2.DailyExpenseMemo;

import android.app.TabActivity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.TabHost;

public class DailyExpenseMemoActivity extends TabActivity {
	
	static final String BOOK_ID_KEY = "book_id";
	
	public static void saveBookId(ContextWrapper cw, long bookId)
	{
		SharedPreferences prefs = cw.getSharedPreferences("Book", MODE_PRIVATE);
		Editor ed = prefs.edit();
		ed.putLong(BOOK_ID_KEY, bookId);
		ed.commit();
	}
	
	public static long getBookId(ContextWrapper cw)
	{
		SharedPreferences prefs = cw.getSharedPreferences("Book", MODE_PRIVATE);
		return prefs.getLong(BOOK_ID_KEY, -1);		
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        long bookId = getBookId(this);
		
		if(-1 == bookId)
		{
			Intent intent = new Intent(this, BookActivity.class);
			startActivity(intent);
			finish();
			return;
		}
        
        
        
        final TabHost tabHost = getTabHost();
        
        tabHost.addTab(tabHost.newTabSpec("tab1")
                .setIndicator("Entry")
                .setContent(new Intent(this, EntryActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
        tabHost.addTab(tabHost.newTabSpec("tab2")
                .setIndicator("History")
                .setContent(new Intent(this, HistoryActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
        tabHost.addTab(tabHost.newTabSpec("tab3")
                .setIndicator("Summary")
                .setContent(new Intent(this, SummaryActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
        tabHost.addTab(tabHost.newTabSpec("tab4")
                .setIndicator("Category")
                .setContent(new Intent(this, CategoryActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));        
        
    }
}