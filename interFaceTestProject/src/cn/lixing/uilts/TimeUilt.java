package cn.lixing.uilts;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUilt {
	private static DateFormat dateFormat;
	private static String dateString;
	private static Date timeDate;
	private static Timestamp dateTime;
	
	public final static Timestamp stringToTime() throws ParseException{
		dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.ENGLISH); 
		dateFormat.setLenient(false); 
		dateString=dateFormat.format(new Date());
			  
		timeDate = dateFormat.parse(dateString);
		dateTime = new java.sql.Timestamp(timeDate.getTime());
		return dateTime;   
	}
//	public static void main(String[] args) throws ParseException {
//		System.out.println(stringToTime());
//	}
}
