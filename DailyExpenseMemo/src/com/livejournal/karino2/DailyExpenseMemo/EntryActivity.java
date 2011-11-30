package com.livejournal.karino2.DailyExpenseMemo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class EntryActivity extends Activity {
	private static final int REQUEST_PICK_FILE = 1;
	
	Database database;
	
	static class NumberButtonHandler implements OnClickListener
	{
		int number;
		EditText target;
		NumberButtonHandler(int num, EditText target)
		{
			this.number = num;
			this.target = target;
		}

		@Override
		public void onClick(View v) {
			if(target.getText().toString().equals("0"))
			{
				target.setText(String.valueOf(number));
				return;
			}
			target.append(String.valueOf(number));
		}
		
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		database = new Database();
		database.open(this);
		
        setContentView(R.layout.entry);

        setUpCategorySpinner();
        setUpTenKeys();
        setUpDateNavigater();
        
        ((Button)findViewById(R.id.save_button)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				save();
				showMessage("saved");
				clearEntry();
			}
        	
        });
    }
    
    private void clearEntry() {
    	((CheckBox)findViewById(R.id.business_checkbox)).setChecked(false);
    	((EditText)findViewById(R.id.money_edit)).setText("0");
    	((EditText)findViewById(R.id.memo_edit)).setText("");    	
	}

	private void setUpDateNavigater() {
    	setDate(new Date());
		
		setButtonOnClickListener(R.id.prev_date_button, new OnClickListener(){

			@Override
			public void onClick(View v) {
				Date dt = getDate();
				Date prev = prevDate(dt);
				setDate(prev);
			}});

		setButtonOnClickListener(R.id.next_date_button, new OnClickListener(){

			@Override
			public void onClick(View v) {
				Date dt = getDate();
				Date prev = nextDate(dt);
				setDate(prev);
			}});
	}

	void setDate(Date today) {
		SimpleDateFormat  sdf = new SimpleDateFormat("yyyy/MM/dd");
		EditText et = (EditText)findViewById(R.id.date_entry_edit);
		et.setText(sdf.format(today));
	}

	private String getETText(int rid)
    {
    	return ((EditText)findViewById(rid)).getText().toString();
    }
    
    private void save() {
    	Entry ent = generateEntry();
    	database.insert(ent);
    }
    
    private Entry generateEntry() {
    	Date date = getDate();
    	long category = getCategorySpinner().getSelectedItemId();
    	int price = Integer.valueOf(getETText(R.id.money_edit));
    	String memo = getETText(R.id.memo_edit);
    	boolean isBusiness = ((CheckBox)findViewById(R.id.business_checkbox)).isChecked();
    	
    	return new Entry(date, category, memo, price, DailyExpenseMemoActivity.getBookId(this), isBusiness);
    }

	Date getDate() {
		Date date = new Date(getETText(R.id.date_entry_edit));
		return date;
	}
    
    private void setButtonOnClickListener(int rid, OnClickListener listener)
    {
		((Button)findViewById(rid)).setOnClickListener(listener);    	
    }

	private void setUpTenKeys() {
		final EditText target = (EditText)findViewById(R.id.money_edit);
		setButtonOnClickListener(R.id.button_0, new NumberButtonHandler(0, target));
		setButtonOnClickListener(R.id.button_1, new NumberButtonHandler(1, target));
		setButtonOnClickListener(R.id.button_2, new NumberButtonHandler(2, target));
		setButtonOnClickListener(R.id.button_3, new NumberButtonHandler(3, target));
		setButtonOnClickListener(R.id.button_4, new NumberButtonHandler(4, target));
		setButtonOnClickListener(R.id.button_5, new NumberButtonHandler(5, target));
		setButtonOnClickListener(R.id.button_6, new NumberButtonHandler(6, target));
		setButtonOnClickListener(R.id.button_7, new NumberButtonHandler(7, target));
		setButtonOnClickListener(R.id.button_8, new NumberButtonHandler(8, target));
		setButtonOnClickListener(R.id.button_9, new NumberButtonHandler(9, target));
		setButtonOnClickListener(R.id.bs_button, new OnClickListener() {			
			@Override
			public void onClick(View v) {
				String text = target.getText().toString();
				if(text.length() >= 1)
					target.getText().delete(text.length()-1, text.length());
			}
		});
		((Button)findViewById(R.id.del_button)).setEnabled(false);
	}

	void setUpCategorySpinner() {
		Spinner spinner = getCategorySpinner();
        
        Cursor cursor = database.fetchCategoriesCursor();
        startManagingCursor(cursor);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, cursor,
        		new String[]{"NAME"}, new int[] {android.R.id.text1});
        
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	Spinner getCategorySpinner() {
		Spinner spinner = (Spinner)findViewById(R.id.category_spinner);
		return spinner;
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
    		String path = data.getData().getPath();
    		EntryStore store = new EntryStore(database);
    		store.setCategoryMap(database.fetchCategories());
    		CsvImporter importer = new CsvImporter(DailyExpenseMemoActivity.getBookId(this), store);
    		showMessage("import: " + path);
    		try {
				importer.importCsv(path);
			} catch (IOException e) {
				e.printStackTrace();
				showMessage("IO exception while reading!");
			} catch( RuntimeException re)
			{
				showMessage("RuntimeException while reading csv: "+ re.getMessage());
			}
    		break;
    	}
    	super.onActivityResult(requestCode, resultCode, data);
    }
	void showMessage(String message) {
		Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
		toast.show();
	}

	public static Date prevDate(Date dt) {
		return DateUtility.prevDate(dt);
	}
	public static Date nextDate(Date dt) {
		return DateUtility.nextDate(dt);
	}
	
	// Only for test purpose. Activity can't refer from JUnit.
	public static class DateUtility
	{
		public static Date prevDate(Date dt) {
			long mili = dt.getTime();
			return new Date(mili-24*60*60*1000);
		}
		public static Date nextDate(Date dt) {
			long mili = dt.getTime();
			return new Date(mili+24*60*60*1000);
		}		
	}
}
