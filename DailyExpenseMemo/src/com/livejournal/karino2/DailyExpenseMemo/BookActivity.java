package com.livejournal.karino2.DailyExpenseMemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class BookActivity extends ListActivity {
	static final int INPUT_DIALOG_ID = 1;
	Database database;
	Cursor cursor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		database = new Database();
		database.open(this);
		cursor = database.fetchBooksCursor();
		
		startManagingCursor(cursor);
		setListAdapter(new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,
				cursor, new String[] {"NAME"}, new int[] {android.R.id.text1 }));
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, int position,
					long arg3) {
	            final Cursor c = (Cursor) parent.getItemAtPosition(position);
	            long bookId = c.getLong(0);
	            DailyExpenseMemoActivity.saveBookId(BookActivity.this, bookId);
	            
				Intent intent = new Intent(BookActivity.this, DailyExpenseMemoActivity.class);
				startActivity(intent);
				finish();
			}});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_menu, menu);
		return super.onCreateOptionsMenu(menu);		
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id)
		{
		case INPUT_DIALOG_ID:
            LayoutInflater factory = LayoutInflater.from(this);
            final View textEntryView = factory.inflate(R.layout.new_book_text_entry, null);
			return new AlertDialog.Builder(this).setTitle("New Book")
			.setView(textEntryView)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	EditText et = (EditText)textEntryView.findViewById(R.id.book_name_edit);
                	String newBookName = et.getText().toString();
                	database.newBook(newBookName);
                	cursor.requery();
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            })
            .create();
		}
		return super.onCreateDialog(id);
	}
	void showMessage(String message) {
		Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
		toast.show();
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId())
		{
		case R.id.menu_new_book_item:
			showDialog(INPUT_DIALOG_ID);
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
}
