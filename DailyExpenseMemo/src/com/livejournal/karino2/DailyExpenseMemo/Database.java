package com.livejournal.karino2.DailyExpenseMemo;

import java.util.Hashtable;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

public class Database {
    private static final String DATABASE_NAME = "daily_expense.db";
    private static final int DATABASE_VERSION = 1;
    private static final String ENTRY_TABLE_NAME = "entry";
    private static final String CATEGORY_TABLE_NAME = "category";
    private static final String BOOK_TABLE_NAME = "book";
    private static final String TAG = "DailyExpenseDatabase";
	
	
    static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + BOOK_TABLE_NAME + " ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "NAME TEXT"
                    + ");");
            db.execSQL("CREATE TABLE " + CATEGORY_TABLE_NAME + " ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "NAME TEXT"
                    + ");");
            db.execSQL("CREATE TABLE " + ENTRY_TABLE_NAME + " ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "DATE INTEGER,"
                    + "CATEGORY INTEGER,"
                    + "MEMO TEXT,"
                    + "PRICE INTEGER,"
                    + "BOOK INTEGER"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            recreate(db);
        }

		public void recreate(SQLiteDatabase db) {
			db.execSQL("DROP TABLE IF EXISTS " + BOOK_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + ENTRY_TABLE_NAME);
            onCreate(db);
		}		
    }

    DatabaseHelper dbHelper;
    SQLiteDatabase database;
	public void open(Context context) {
    	dbHelper = new DatabaseHelper(context);
    	database = dbHelper.getWritableDatabase();
    }
	
	public void close() {
		dbHelper.close();
	}
	
	public void recreate() {
		dbHelper.recreate(database);
	}

	public Cursor fetchAllEntry(String selection, String[] selectionArgs) {
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		// use table name directly for simplicity.
		/*
		builder.setTables("entry LEFT OUTER JOIN category ON (entry.CATEGORY = category._id) "+
				" LEFT OUTER JOIN book ON (entry.BOOK = book._id)");
				*/
		builder.setTables("entry LEFT OUTER JOIN category ON (entry.CATEGORY = category._id) ");
		
		return builder.query(database, new String[] {
		"_id", "DATE", "category.NAME", "MEMO", "PRICE"  
		}, null, null, null, null, "DATE DESC");
	}
	
	public Hashtable<String, Integer> fetchBooks() {
		Cursor cursor = database.query(true, BOOK_TABLE_NAME,
				new String[]{"_id", "SENKEI"}, null, null, null, null, null, null);
		Hashtable<String, Integer> map = new Hashtable<String, Integer>();
		while(!cursor.isLast())
		{
			cursor.moveToNext();
			map.put(cursor.getString(1), cursor.getInt(0));
		}
		cursor.close();
		return map;
	}
	


}
