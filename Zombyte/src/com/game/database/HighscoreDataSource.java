package com.game.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class HighscoreDataSource {

	// Database fields
	private SQLiteDatabase database;
	private HighscoreDatabaseHelper dbHelper;
	private String[] allColumns = { 
			HighscoreDatabaseHelper.COLUMN_ID,
			HighscoreDatabaseHelper.COLUMN_NAME,
			HighscoreDatabaseHelper.COLUMN_SCORE };

	public HighscoreDataSource(Context context) {
		dbHelper = new HighscoreDatabaseHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Highscore createHighscore(Highscore highscore) {
		ContentValues values = new ContentValues();
		values.put(HighscoreDatabaseHelper.COLUMN_NAME, highscore.getName());
		values.put(HighscoreDatabaseHelper.COLUMN_SCORE,	highscore.getScore());

		long insertId = database.insert(HighscoreDatabaseHelper.TABLE_HIGHSCORE, null, values);
		
		// To show how to query
		Cursor cursor = database.query(HighscoreDatabaseHelper.TABLE_HIGHSCORE,
				allColumns, HighscoreDatabaseHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		return cursorToHighscore(cursor);
	}

	public List<Highscore> getAllHighscores() {
		List<Highscore> highscoreList = new ArrayList<Highscore>();
		Cursor cursor = database.query(HighscoreDatabaseHelper.TABLE_HIGHSCORE,
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Highscore highscore = cursorToHighscore(cursor);
			highscoreList.add(highscore);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return highscoreList;
	}

	private Highscore cursorToHighscore(Cursor cursor) {
		Highscore highscore = new Highscore(cursor.getString(1), cursor.getInt(2));
		
		return highscore;
	}
	
	
//	public void backupDb() throws IOException {
//	    try {
//	        File sd = Environment.getExternalStorageDirectory();
//	        File data = Environment.getDataDirectory();
//
//	        if (sd.canWrite()) {
//	            String currentDBPath = "/data/tv.countdown/databases/tvcountdown.db";
//	            String backupDBPath = "/TVCountdown/tvcountdown.db";
//	            File currentDB = new File(data, currentDBPath);
//	            File backupDB = new File(sd, backupDBPath);
//
//
//	            if (currentDB.exists()) {
//	                FileChannel src = new FileInputStream(currentDB).getChannel();
//	                FileChannel dst = new FileOutputStream(backupDB).getChannel();
//	                dst.transferFrom(src, 0, src.size());
//	                src.close();
//	                dst.close();
//	            }
//	        }
//	    } catch (Exception e) {
//	    }
//	}
	
	public void deleteDabase() {
		dbHelper.deleteDatabase();
	}

}
