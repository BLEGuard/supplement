package com.example.lanyashouji;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class Sqlite_Helper extends SQLiteOpenHelper {
    public static final String MY_SQLite1 = "create table RSSI1("
            +"id integer primary key autoincrement, "
            +"device text, "
            +"curtime text, "
            +"location text, "
            +"dx integer, "
            +"dy integer, "
            +"name text, "
            +"address text, "
            +"RSSI integer, "
            +"error integer, "
            +"RSSI2_id integer)";//版本3的更新，添加新的一列
    public static final String MY_SQLite2 = "create table RSSI2("
            +"id integer primary key autoincrement, "
            +"device text, "
            +"curtime text, "
            +"location text, "
            +"dx integer, "
            +"dy integer, "
            +"name text, "
            +"address text, "
            +"RSSI integer, "
            +"error integer, "
            +"RSSI3_id integer)";//版本3的更新，添加新的一列
    public static final String MY_SQLite3 = "create table RSSI3("
            +"id integer primary key autoincrement, "
            +"device text, "
            +"curtime text, "
            +"location text, "
            +"dx integer, "
            +"dy integer, "
            +"name text, "
            +"address text, "
            +"RSSI integer, "
            +"error integer, "
            +"RSSI4_id integer)";//版本3的更新，添加新的一列
    public static final String MY_SQLite4 = "create table RSSI4("
            +"id integer primary key autoincrement, "
            +"device text, "
            +"curtime text, "
            +"location text, "
            +"dx integer, "
            +"dy integer, "
            +"name text, "
            +"address text, "
            +"RSSI integer, "
            +"error integer, "
            +"RSSI5_id integer)";//版本3的更新，添加新的一列
    public static final String MY_SQLite5 = "create table RSSI5("
            +"id integer primary key autoincrement, "
            +"device text, "
            +"curtime text, "
            +"location text, "
            +"dx integer, "
            +"dy integer, "
            +"name text, "
            +"address text, "
            +"RSSI integer, "
            +"error integer, "
            +"RSSI6_id integer)";//版本3的更新，添加新的一列
    public static final String MY_SQLite6 = "create table RSSI6("
            +"id integer primary key autoincrement, "
            +"device text, "
            +"curtime text, "
            +"location text, "
            +"dx integer, "
            +"dy integer, "
            +"name text, "
            +"address text, "
            +"RSSI integer, "
            +"error integer, "
            +"RSSI7_id integer)";//版本3的更新，添加新的一列
    public static final String MY_SQLite7 = "create table RSSI7("
            +"id integer primary key autoincrement, "
            +"device text, "
            +"curtime text, "
            +"location text, "
            +"dx integer, "
            +"dy integer, "
            +"name text, "
            +"address text, "
            +"RSSI integer, "
            +"error integer, "
            +"RSSI8_id integer)";//版本3的更新，添加新的一列
    public static final String MY_SQLite8 = "create table RSSI8("
            +"id integer primary key autoincrement, "
            +"device text, "
            +"curtime text, "
            +"location text, "
            +"dx integer, "
            +"dy integer, "
            +"name text, "
            +"address text, "
            +"RSSI integer, "
            +"error integer, "
            +"RSSI9_id integer)";//版本3的更新，添加新的一列
    public static final String MY_SQLite9 = "create table RSSI9("
            +"id integer primary key autoincrement, "
            +"device text, "
            +"curtime text, "
            +"location text, "
            +"dx integer, "
            +"dy integer, "
            +"name text, "
            +"address text, "
            +"RSSI integer, "
            +"error integer, "
            +"RSSI10_id integer)";//版本3的更新，添加新的一列
    public static final String MY_SQLite10 = "create table RSSI10("
            +"id integer primary key autoincrement, "
            +"device text, "
            +"curtime text, "
            +"location text, "
            +"dx integer, "
            +"dy integer, "
            +"name text, "
            +"address text, "
            +"RSSI integer, "
            +"error integer, "
            +"RSSI11_id integer)";//版本3的更新，添加新的一列
    public static final String MY_SQLite11 = "create table RSSI11("
            +"id integer primary key autoincrement, "
            +"device text, "
            +"curtime text, "
            +"location text, "
            +"dx integer, "
            +"dy integer, "
            +"name text, "
            +"address text, "
            +"RSSI integer, "
            +"error integer, "
            +"RSSI12_id integer)";//版本3的更新，添加新的一列
    public static final String MY_SQLite12 = "create table RSSI12("
            +"id integer primary key autoincrement, "
            +"device text, "
            +"curtime text, "
            +"location text, "
            +"dx integer, "
            +"dy integer, "
            +"name text, "
            +"address text, "
            +"RSSI integer, "
            +"error integer, "
            +"RSSI13_id integer)";//版本3的更新，添加新的一列
    public static final String MY_SQLite13 = "create table RSSI13("
            +"id integer primary key autoincrement, "
            +"device text, "
            +"curtime text, "
            +"location text, "
            +"dx integer, "
            +"dy integer, "
            +"name text, "
            +"address text, "
            +"RSSI integer, "
            +"error integer, "
            +"RSSI14_id integer)";//版本3的更新，添加新的一列
    public static final String MY_SQLite14 = "create table RSSI14("
            +"id integer primary key autoincrement, "
            +"device text, "
            +"curtime text, "
            +"location text, "
            +"dx integer, "
            +"dy integer, "
            +"name text, "
            +"address text, "
            +"RSSI integer, "
            +"error integer, "
            +"RSSI15_id integer)";//版本3的更新，添加新的一列
    public static final String MY_SQLite15 = "create table RSSI15("
            +"id integer primary key autoincrement, "
            +"device text, "
            +"curtime text, "
            +"location text, "
            +"dx integer, "
            +"dy integer, "
            +"name text, "
            +"address text, "
            +"RSSI integer, "
            +"error integer, "
            +"RSSI1_id integer)";//版本3的更新，添加新的一列
	/*public static final String MY_SQLite2 = "create table RSSI_2("//版本2，添加新表格
			+"id integer primary key autoincrement, "
			+"name text, "
			+"address text, "
			+"RSSI integer)";*/

    private Context mContext;

    public Sqlite_Helper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//第一次使用数据库自动建表
        // TODO Auto-generated method stub
        db.execSQL(MY_SQLite1);
        db.execSQL(MY_SQLite2);
        db.execSQL(MY_SQLite3);
        db.execSQL(MY_SQLite4);
        db.execSQL(MY_SQLite5);
        db.execSQL(MY_SQLite6);
        db.execSQL(MY_SQLite7);
        db.execSQL(MY_SQLite8);
        db.execSQL(MY_SQLite9);
        db.execSQL(MY_SQLite10);
        db.execSQL(MY_SQLite11);
        db.execSQL(MY_SQLite12);
        db.execSQL(MY_SQLite13);
        db.execSQL(MY_SQLite14);
        db.execSQL(MY_SQLite15);
        Toast.makeText(mContext, "DB RSSI1-15 Create Succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
		/*db.execSQL("drop table if exists RSSI");//如果存在此表格，删除，对于想添加新表格的话，有用
		onCreate(db);//重新画表格*/
        //if(oldVersion <= newVersion){
		/*	switch (oldVersion) {
			case 1:
				if(1 < newVersion)
					db.execSQL(MY_SQLite2);//更新版本的话，2版本，建立新表格，没有break，为了所有更新都成功
			case 2:
				if(2 < newVersion)
					db.execSQL(MY_SQLite3);//更新版本的话，2版本，建立新表格，没有break，为了所有更新都成功
			case 3:
				if(3 < newVersion)
					db.execSQL(MY_SQLite4);//更新版本的话，2版本，建立新表格，没有break，为了所有更新都成功
			case 4:
				if(4 < newVersion)
					db.execSQL(MY_SQLite5);//更新版本的话，2版本，建立新表格，没有break，为了所有更新都成功
			case 5:
				if(5 < newVersion)
					db.execSQL(MY_SQLite6);//更新版本的话，2版本，建立新表格，没有break，为了所有更新都成功
			case 2:
				db.execSQL("alter table RSSI add column RSSI2_id integer");//更新版本3，在RSSI表格中添加新的一列column，为RSSI2_id
			default:
			}*/
		/*}else{  version版本号不能变低，会报错，下列语句没用
			db.execSQL("drop table if exists RSSI1");//如果存在此表格，删除，对于想添加新表格的话，有用
			db.execSQL("drop table if exists RSSI2");//如果存在此表格，删除，对于想添加新表格的话，有用
			db.execSQL("drop table if exists RSSI3");//如果存在此表格，删除，对于想添加新表格的话，有用
			db.execSQL("drop table if exists RSSI4");//如果存在此表格，删除，对于想添加新表格的话，有用
			db.execSQL("drop table if exists RSSI5");//如果存在此表格，删除，对于想添加新表格的话，有用
			db.execSQL("drop table if exists RSSI6");//如果存在此表格，删除，对于想添加新表格的话，有用
			onCreate(db);//重新画表格
			Toast.makeText(mContext, "重新建立表格，RSSI1 succeed again", Toast.LENGTH_SHORT).show();
		}*/
    }

}
