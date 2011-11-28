package com.livejournal.karino2.DailyExpenseMemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class EntryActivity extends Activity {
	private static final int REQUEST_PICK_FILE = 1;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry);

    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.entry_menu, menu);    	
    	return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	switch(item.getItemId())
    	{
    	case R.id.menu_import_item:
            startActivityForResult(new Intent(this, FilePickerActivity.class), REQUEST_PICK_FILE);
    		break;
    	}
    	return super.onMenuItemSelected(featureId, item);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch(requestCode)
    	{
    	case REQUEST_PICK_FILE:
    		// start import.
    		showMessage(data.getData().getPath());
    		break;
    	}
    	super.onActivityResult(requestCode, resultCode, data);
    }
	void showMessage(String message) {
		Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
		toast.show();
	}
}
