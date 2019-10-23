package com.fgtit.app;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class SqliteDB {

	private SQLiteDatabase db;

	public void open(){
		db= SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + "/Robotime/robotime.db",null);
	}

	public void createTable(){
		//������SQL��� define the SQL statement of create table
		String stu_table="create table usertable(_id integer primary key autoincrement,sname text,snumber text)";
		//ִ��SQL���  execute SQL statement
		db.execSQL(stu_table);

		String sql="CREATE TABLE TB_USERS(userid CHAR[16] PRIMARY KEY ASC,"
				+ "cardsn	CHAR[32],"
				+ "username CHAR[24],"
				+ "password CHAR[16],"
				+ "userstyle INTEGER,"
				+ "identifytype INTEGER,"
				+ "groupid INTEGER,"
				+ "jobtype INTEGER,"
				+ "depttype INTEGER,"
				+ "leveltype INTEGER,"
				+ "usetype INTEGER,"
				+ "enroldate DATE,"
				+ "expdate DATE,"
				+ "remarks VARCHAR(100),"
				+ "finger1 INTEGER,"
				+ "finger2 INTEGER,"
				+ "photo BLOB,"
				+ "template1 BLOB,"
				+ "template2 BLOB);";

		db.execSQL(sql);
	}

	public void insertData(){
		//ʵ��������ֵ get instance of content values
		ContentValues cValue = new ContentValues();
		//����û��� insert username
		cValue.put("sname","xiaoming");
		//�������  insert password
		cValue.put("snumber","01005");
		//����insert()������������  call insert()method for adding data into database
		db.insert("stu_table",null,cValue);
	}

	public void insert(){
		//��������SQL��� SQL statement of inserting data
		String stu_sql="insert into stu_table(sname,snumber) values('xiaoming','01005')";
		//ִ��SQL��� execute SQL statement
		db.execSQL(stu_sql);
	}

	public void deleteData() {
		//ɾ������ delete condition
		String whereClause = "id=?";
		//ɾ���������� delete value of condition
		String[] whereArgs = {String.valueOf(2)};
		//ִ��ɾ�� execute delete SQL
		db.delete("stu_table",whereClause,whereArgs);
	}

	public void delete() {
		//ɾ��SQL��� delete SQL statement
		String sql = "delete from stu_table where _id = 6";
		//ִ��SQL��� execute SQL
		db.execSQL(sql);
	}

	public void updateData() {
		//ʵ��������ֵ
		ContentValues values = new ContentValues();
		//��values��������� add value to contentValues
		values.put("snumber","101003");
		//�޸����� modify the condition
		String whereClause = "id=?";
		//�޸���Ӳ��� modify the parameter
		String[] whereArgs={String.valueOf(1)};
		//�޸� update data
		db.update("usertable",values,whereClause,whereArgs);
	}

	public void update(){
		//�޸�SQL��� update SQL statement
		String sql = "update stu_table set snumber = 654321 where id = 1";
		//ִ��SQL execute
		db.execSQL(sql);
	}

	public void drop(){
		//ɾ�����SQL���  delete table
		String sql ="DROP TABLE stu_table";
		//ִ��SQL
		db.execSQL(sql);
	}

	/*
	getCount()	����ܵ��������� get the number of data
 	isFirst()	�ж��Ƿ��һ����¼ check if it is first record
 	isLast()	�ж��Ƿ����һ����¼ if it is last record
 	moveToFirst()	�ƶ�����һ����¼
 	moveToLast()	�ƶ������һ����¼
 	move(int offset)	�ƶ���ָ����¼
 	moveToNext()	�ƶ�����һ����¼
 	moveToPrevious()	�ƶ�����һ����¼
 	getColumnIndexOrThrow(String  columnName)	���������ƻ��������
 	getInt(int columnIndex)	���ָ����������int����ֵ
 	getString(int columnIndex)	���ָ������Ӱ��String����ֵ
	 */
	public void query(SQLiteDatabase db) {
		//��ѯ����α�
		Cursor cursor = db.query ("usertable",null,null,null,null,null,null);

		//�ж��α��Ƿ�Ϊ��
		if(cursor.moveToFirst()){
			//�����α�
			for(int i=0;i<cursor.getCount();i++){
				cursor.move(i);
				//���ID
				int id = cursor.getInt(0);
				//����û���
				String username=cursor.getString(1);
				//�������
				String password=cursor.getString(2);
				//����û���Ϣ System.out.println(id+":"+sname+":"+snumber); 
			}
		}
	}
}
