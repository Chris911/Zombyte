package com.game.database;
 
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class HighscoreDatabaseHelper extends SQLiteOpenHelper {
	
	
	public static final String DATABASE_NAME = "zombyte.db";
	private static final int DATABASE_VERSION = 1;
	
	private Context context;
 
	public static final String TABLE_HIGHSCORE = "highscore";
	public static final String COLUMN_ID = "ID";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_SCORE = "score";	
 
	private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_HIGHSCORE + " ("
	+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
	+ COLUMN_NAME + " TEXT NOT NULL,"
	+ COLUMN_SCORE + " INTEGER NOT NULL);";
	
	public HighscoreDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(HighscoreDatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_HIGHSCORE);
		onCreate(db);
	}
	
	public void deleteDatabase() {
		context.deleteDatabase(DATABASE_NAME);
	}
 
}