package com.livejournal.karino2.DailyExpenseMemo;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class DailyExpenseMemoActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        final TabHost tabHost = getTabHost();

        tabHost.addTab(tabHost.newTabSpec("tab1")
                .setIndicator("hoge")
                .setContent(new Intent(this, EntryActivity.class)));
        
    }
}