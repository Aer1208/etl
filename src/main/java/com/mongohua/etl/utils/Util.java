package com.mongohua.etl.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 * @author xiaohf
 */
public class Util {

	public static SimpleDateFormat YYYY = new SimpleDateFormat("yyyy");
	public static SimpleDateFormat YYYYMM=new SimpleDateFormat("yyyyMM");
	public static SimpleDateFormat YYYYMMDD=new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat YYYYMMDDHH = new SimpleDateFormat("yyyyMMddHH");
	public static SimpleDateFormat YYYYMMDDHHMM = new SimpleDateFormat("yyyyMMddHHmm");
	
	/**
	 * 
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static synchronized int daysOfWeek(String dateStr) throws ParseException {
		Calendar cal = get(dateStr);
		return cal.get(Calendar.DAY_OF_WEEK) - 1;
	}
	
	public static synchronized String firstDayOfWeek(String dateStr) throws ParseException {
		Calendar cal = get(dateStr);
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayofweek == 1) {
			dayofweek += 7;
		}
		cal.add(Calendar.DATE, 2 - dayofweek);
		return YYYYMMDD.format(cal.getTime());
		
	}
	
	public static synchronized Calendar get(String dateStr) throws ParseException{
		Date date ;
		if (dateStr.length() == 4) {
		    date = YYYY.parse(dateStr);
        }else if (dateStr.length() == 6) {
			date = YYYYMM.parse(dateStr);
		} else if (dateStr.length() == 8) {
			date = YYYYMMDD.parse(dateStr);
		} else if (dateStr.length() == 10) {
			date = YYYYMMDDHH.parse(dateStr);
		} else if (dateStr.length() == 12){
		    date = YYYYMMDDHHMM.parse(dateStr);
        } else{
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static synchronized String dateIns(String dateStr, int pos, int type) throws ParseException {
		Calendar cal = get(dateStr);
		if (type == Constant.HOUR) {
			cal.add(Calendar.HOUR, pos);
            return YYYYMMDDHH.format(cal.getTime());
		} else if (type == Constant.DAY) {
			cal.add(Calendar.DATE, pos);
            return YYYYMMDD.format(cal.getTime());
		} else if (type == Constant.MONTH) {
			cal.add(Calendar.MONTH, pos);
            return YYYYMM.format(cal.getTime());
		} else if (type == Constant.YEAR ) {
			cal.add(Calendar.YEAR, pos);
            return YYYY.format(cal.getTime());
		}
		return YYYYMMDDHHMM.format(cal.getTime());
	}

	public static synchronized String dateIns(int pos, int type) throws ParseException {
		String dateStr = YYYYMMDDHHMM.format(new Date());

		return dateIns(dateStr,pos,type);
	}
	
	public static synchronized String lastDayOfWeek(String dateStr) throws ParseException {
		Calendar cal = get(dateStr);
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK) ;
		if (dayofweek == 1) {
			dayofweek += 7;
		} 
		cal.add(Calendar.DATE, 8 - dayofweek);
		return YYYYMMDD.format(cal.getTime());
	}

	public static synchronized Integer dateDiff(String start, String end) throws ParseException {
		Calendar startCal = get(start);
		Calendar endCal = get(end);
		return (int) ((endCal.getTimeInMillis() - startCal.getTimeInMillis()) / (24*60*60*1000)) ;
	}

	/**
	 * 获取当前时间
	 * @return
	 */
	public static synchronized long getCurrentTime() {
		return System.currentTimeMillis();
	}

	/**
	 * 获取日期所在月的天数
	 * @param vDate
	 * @return
	 */
	public static int getDaysOfMonth(String vDate) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(YYYYMM.parse(vDate.substring(0,6)));
			return cal.getActualMaximum(Calendar.DATE);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1;
	}

    /**
     * 对命令进行隐藏密码处理
     * @param cmd
     * @return
     */
	public static String hidePassword(String cmd) {
        Pattern p = Pattern.compile("([\\S|\\s]+\\s*--password,\\s+)([^\\s]+\\s+)([\\S|\\s]+)");
        Matcher m = p.matcher(cmd);
        if (m.matches()) {
            return m.group(1) + " ****** " + m.group(3);
        }else {
            return cmd;
        }
    }

}
