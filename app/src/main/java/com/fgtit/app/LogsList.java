package com.fgtit.app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.fgtit.utils.DateTool;

import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LogsList {

	private static LogsList instance;
	public static LogsList getInstance() {
		if(null == instance) {
			instance = new LogsList();
		}
		return instance;
	}

	public List<LogItem> logsList=new ArrayList<LogItem>();
	private SQLiteDatabase db;

	public static boolean IsFileExists(String filename){
		File f=new File(filename);
		if(f.exists()){
			return true;
		}
		return false;
	}

	public void Init(){
		if(IsFileExists(Environment.getExternalStorageDirectory() + "/Robotime/logs1.db")){
			db= SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + "/Robotime/logs1.db",null);
		}else{
			db= SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + "/Robotime/logs1.db",null);
			String sql="CREATE TABLE TB_LOGS(_id integer primary key autoincrement," +
					"userid INTEGER,"
					+ "username CHAR[24],"
					+ "status1 INTEGER,"
					+ "status2 INTEGER,"
					+"fingertype INTEGER,"
					+"imei CHAR[24],"
					+"state INTEGER,"
					+"currenttime CHAR[],"
					+"canteentype CHAR[24],"
					+"workcode CHAR[24],"
					+ "datetime DATETIME);";

			db.execSQL(sql);
		}
	}

	public void Clear(){
		logsList.clear();
		String sql = "delete from TB_LOGS";
		db.execSQL(sql);
	}

	public void deleteLog(int id){
		db.delete("TB_LOGS", "_id=?", new String[] { String.valueOf(id) });
	}

	public String getStringDate() {
		Date currentTime = new Date(System.currentTimeMillis());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public String setDateFormat(String timeStamp){
		String timeString = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long  l = Long.valueOf(timeStamp);
		timeString = sdf.format(new Date(l));//单位秒
		return timeString;
	}

	public void Append(int userid, String username, int statu1, int statu2, int fingertype, String imei, int state,String workcode, String canteentype){
		String sql="insert into TB_LOGS(userid,username,status1,status2,fingertype,imei,state,currenttime,canteentype,workcode,datetime) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?)";
		//String datetime=getStringDate();

		Date currentTime = new Date(System.currentTimeMillis());
		Timestamp timestamp = new Timestamp(currentTime.getTime());

		Object[] args = new Object[]{userid,username,statu1,statu2,fingertype,imei,state,String.valueOf(timestamp),canteentype,workcode,timestamp};
		db.execSQL(sql,args);
	}

	public List<LogItem> LoadAll(){
		logsList.clear();
//		"select * from TB_LOGS where datetime(datetime) between datetime('"
//				+startTime+"') and datetime('"+endTime+"')";
		Cursor cursor = db.query ("TB_LOGS",null,null,null,null,null,null);
		if(cursor!=null){
			if(cursor.moveToFirst()){
				for(int i=0;i<cursor.getCount();i++){
					LogItem li=new LogItem();
					li.id=cursor.getInt(0);
					li.userid=cursor.getInt(1);
					li.username=cursor.getString(2);
					li.status1=cursor.getInt(3);
					li.status2=cursor.getInt(4);
					li.fingertype=cursor.getInt(5);
					li.imei=cursor.getString(6);
					li.state=cursor.getInt(7);
					li.currenttime=cursor.getString(8);
					li.canteentype=cursor.getString(9);
					li.workcode=cursor.getString(10);
					li.datetime=cursor.getString(11);
					logsList.add(li);
					cursor.moveToNext();
				}
			}
			cursor.close();
		}
		return logsList;
	}


	public void updateStateByUserId(int id ) {
		//�޸�SQL��� update SQL statemen
		db.execSQL("update TB_LOGS set status1=1 where userid=?", new Object[]{id});
	}


	public List<LogItem> LoadNoUpdateData(){
		logsList.clear();
//		"select * from TB_LOGS where datetime(datetime) between datetime('"
//				+startTime+"') and datetime('"+endTime+"')";
		String[] arr = {0+""};
		Cursor cursor = db.query ("TB_LOGS",null,"status1 = ?",arr,null,null,null);
		if(cursor!=null){
			if(cursor.moveToFirst()){
				for(int i=0;i<cursor.getCount();i++){
					LogItem li=new LogItem();
					li.id=cursor.getInt(0);
					li.userid=cursor.getInt(1);
					li.username=cursor.getString(2);
					li.status1=cursor.getInt(3);
					li.status2=cursor.getInt(4);
					li.fingertype=cursor.getInt(5);
					li.imei=cursor.getString(6);
					li.state=cursor.getInt(7);
					li.currenttime=cursor.getString(8);
					li.canteentype=cursor.getString(9);
					li.workcode=cursor.getString(10);
					li.datetime=cursor.getString(11);
					logsList.add(li);
					cursor.moveToNext();
				}
			}
			cursor.close();
		}
		return logsList;
	}


	public List<LogItem> LoadByTime(String startTime,String endTime){
		logsList.clear();
//		"select * from TB_LOGS where datetime(datetime) between datetime('"
//				+startTime+"') and datetime('"+endTime+"')";
		String arr[] = {startTime,endTime};
		Cursor cursor = db.query ("TB_LOGS",null,"datetime between ? and ?",arr,null,null,null);
		if(cursor!=null){
			if(cursor.moveToFirst()){
				for(int i=0;i<cursor.getCount();i++){
					LogItem li=new LogItem();
					li.id=cursor.getInt(0);
					li.userid=cursor.getInt(1);
					li.username=cursor.getString(2);
					li.status1=cursor.getInt(3);
					li.status2=cursor.getInt(4);
					li.fingertype=cursor.getInt(5);
					li.imei=cursor.getString(6);
					li.state=cursor.getInt(7);
					li.currenttime=cursor.getString(8);
					li.canteentype=cursor.getString(9);
					li.workcode=cursor.getString(10);
					li.datetime=cursor.getString(11);
					logsList.add(li);
					cursor.moveToNext();
				}
			}
			cursor.close();
		}
		return logsList;
	}

	public List<LogItem> QueryById(int userid){
		List<LogItem> list = new ArrayList<>();
		Cursor cursor=db.query("TB_LOGS",null,"userid=?",new String[]{String.valueOf(userid)},null,null,null);
		if(cursor!=null){
			if(cursor.moveToFirst()){
				for(int i=0;i<cursor.getCount();i++){
					LogItem li=new LogItem();
					li.id=cursor.getInt(0);
					li.userid=cursor.getInt(1);
					li.username=cursor.getString(2);
					li.status1=cursor.getInt(3);
					li.status2=cursor.getInt(4);
					li.fingertype=cursor.getInt(5);
					li.imei=cursor.getString(6);
					li.state=cursor.getInt(7);
					li.currenttime=cursor.getString(8);
					li.canteentype=cursor.getString(9);
					li.workcode=cursor.getString(10);
					li.datetime=cursor.getString(11);
					list.add(li);
					cursor.moveToNext();
				}
			}
			cursor.close();
		}
		return list;
	}

	public List<LogItem> Query(int qtype, String qname, String qval){
		logsList.clear();
		switch(qtype){
			case 0:{
				Cursor cursor = db.query ("TB_LOGS",null,null,null,null,null,null);
				if(cursor!=null){
					if(cursor.moveToFirst()){
						for(int i=0;i<cursor.getCount();i++){
							LogItem li=new LogItem();
							li.id=cursor.getInt(0);
							li.userid=cursor.getInt(1);
							li.username=cursor.getString(2);
							li.status1=cursor.getInt(3);
							li.status2=cursor.getInt(4);
							li.fingertype=cursor.getInt(5);
							li.imei=cursor.getString(6);
							li.state=cursor.getInt(7);
							li.currenttime=cursor.getString(8);
							li.canteentype=cursor.getString(9);
							li.workcode=cursor.getString(10);
							li.datetime=cursor.getString(11);
							logsList.add(li);
							cursor.moveToNext();
						}
					}
					cursor.close();
				}
			}
			break;
			case 1:
				break;
			case 2:
				break;
		}
		return logsList;
	}

	public byte[] LogItemToBytes(LogItem li){
		byte[] lb=new byte[10];
		Date date=null;
		int year,month,day,hour,min,sec;

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = (Date)format.parse(li.datetime);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH)+1;	//�·��Ǵ�0��ʼ��
			day = calendar.get(Calendar.DAY_OF_MONTH);
			hour = calendar.get(Calendar.HOUR_OF_DAY);
			min = calendar.get(Calendar.MINUTE);
			sec = calendar.get(Calendar.SECOND);

			lb[0]=(byte) (li.userid&0xFF);
			lb[1]=(byte) ((li.userid>>8)&0xFF);
			lb[2]=(byte) li.status1;
			lb[3]=(byte) li.status2;
			lb[4]=(byte) (year-2000);
			lb[5]=(byte) month;
			lb[6]=(byte) day;
			lb[7]=(byte) hour;
			lb[8]=(byte) min;
			lb[9]=(byte) sec;
			return lb;
		} catch (ParseException e) {
		}
		return null;
	}
}
