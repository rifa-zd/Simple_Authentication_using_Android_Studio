package com.example.cse489_2022_1_60_265;/*
	- Change the class name and DBname, table name, and the attributes name according to the Lecture Summary app
	- Complete all functions (insert, update, ...) considering all attributes
*/

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class AttendanceDB extends SQLiteOpenHelper {

	public AttendanceDB(Context context) {

		super(context, "ClassAttendanceDB.db", null, 1);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("DB@OnCreate");
		String sql = "CREATE TABLE attendance  ("
								+ "name TEXT,"
								+ "course TEXT,"
								+ "datetime INT,"
								+ "status INT,"
								+ "remarks TEXT,"
								+ "PRIMARY KEY(name, course, datetime))";
		db.execSQL(sql);
	}

//	only if version is changed
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("Write code to modify database schema here");
		// db.execSQL("ALTER table my_table  ......");
		// db.execSQL("CREATE TABLE  ......");
	}
	public void insert(StudentAttendance sa, long datetime, String course) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cols = new ContentValues();
		cols.put("name", sa.name);
		cols.put("course", course);
		cols.put("datetime", datetime);
		cols.put("status", sa.status ? 1 : 0);
		cols.put("remarks", sa.remarks);
		db.insert("attendance", null ,  cols);
		db.close();
	}
	public void update(StudentAttendance sa, long datetime, String course) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
//		values.put("name", sa.name);
//		values.put("course", sa.course);
//		values.put("datetime", datetime);
		values.put("status", sa.status ? 1 : 0);
		values.put("remarks", sa.remarks);
		db.update("attendance", values, "name=?,course=?,datetime=?", new String[] {sa.name, course, ""+datetime});
		//, are converted to end
		db.close();
	}
	public void delete(StudentAttendance sa, long datetime, String course) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("attendance", "name=?,course=?,datetime=?", new String[] {sa.name, course, ""+datetime});
		db.close();
	}
	public Cursor select(String query) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor res = null;
		try {
			res = db.rawQuery(query, null);
		} catch (Exception e){
			e.printStackTrace();
		}
		return res;
	}
}