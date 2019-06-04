/*
 * All rights Reserved, Copyright (C) JACK LIMITED 2018
 * FileName: MyUtils.java
 * Version:  1.0
 * Modify record:
 * NO. |     Date        |    Name      |      Content
 * 1   | 2018年9月7日      | JACK)Jack    | original version
 */
package com.jack.hhitseat.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * class name:MyUtils <BR>
 * class description:工具类 <BR>
 * Remark: <BR>
 * @version 1.00 2018年9月7日
 * @author JACK)jackwei
 */
public class MyUtils {
	/**
	 * Method name: isEmail <BR>
	 * Description: 判断是不是邮箱,是就返回true <BR>
	 * Remark: <BR>
	 * @param email
	 * @return  boolean<BR>
	 */
	public static boolean isEmail(String email) {
		String regex = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
		if(email.matches(regex)) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * Method name: isPhoneNum <BR>
	 * Description: 判断手机号是不是正确,是就返回true <BR>
	 * Remark: <BR>
	 * @param phoneNume
	 * @return  boolean<BR>
	 */
	public static boolean isPhoneNum(String phoneNume) {
		String pattern = "^((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8])|(19[7]))\\d{8}$";
		if(phoneNume.matches(pattern)) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * Method name: nowDate <BR>
	 * Description: 返回当前日期和时间yyyy-MM-dd HH:mm:ss <BR>
	 * Remark: <BR>
	 * @return  String<BR>
	 */
	public static String getNowDateTime() {
		String dateTime = "";
		String pattern = "yyyy-MM-dd HH:mm:ss";
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		dateTime = sdf.format(date);
		return dateTime;
	}
	
	/**
	 * Method name: getNowDateYMD <BR>
	 * Description: 返回当前日期和时间 yyyy-MM-dd <BR>
	 * Remark: <BR>
	 * @return  String<BR>
	 */
	public static String getNowDateYMD() {
		String dateTime = "";
		String pattern = "yyyy-MM-dd";
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		dateTime = sdf.format(date);
		return dateTime;
	}
	/**
	 * Method name: getNowDateCHYMD <BR>
	 * Description: 返回当前日期和时间 yyyy年MM月dd日<BR>
	 * Remark: <BR>
	 * @return  String<BR>
	 */
	public static String getNowDateCHYMD() {
		String dateTime = "";
		String pattern = "yyyy年MM月dd日";
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		dateTime = sdf.format(date);
		return dateTime;
	}	/**
	 * Method name: getAutoNumber <BR>
	 * Description: 根据时间获取编号:年月日+4位数字 <BR>
	 * Remark: 格式:201809200001 <BR>  
	 * @return  String<BR>
	 */
	public static synchronized String getAutoNumber() {
		String autoNumber = "";
		int number = 0;
		String oldDate="";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String nowDate = sdf.format(new Date());
		File f2 = new File(MyUtils.class.getResource("").getPath());
		String path = f2.getAbsolutePath();

		File f = new File(path+"/date.txt");
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = "";
			try {
				line=br.readLine();
				String[] sb = line.split(",");
				oldDate = sb[0];
				if(oldDate.equals(nowDate)) {
					number = Integer.parseInt(sb[1]);
				}else {
					number = 0;
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			autoNumber += nowDate;
			number++;
			int i=1;
			int n = number;
			while((n=n/10)!=0) {
				i++;
			}
			for(int j=0; j<4-i; j++) {
				autoNumber+="0";
			}
			autoNumber+=number;
			
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(f));
				bw.write(nowDate+","+number);
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return autoNumber;
	}
	
	/**
	 * Method name: get2DateDay <BR>
	 * Description: 获取两个日期之间的天数 <BR>
	 * Remark: 如2018-09-01 和 2018-09-017  返回就是17天<BR>
	 * @param startDate
	 * @param endDate
	 * @return  int<BR>
	 */
	public static int get2DateDay(String startDate, String endDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = null;
		Date date2 = null;
		long days = 0;
		long yushu = 0;
		
		try {
			date1 = sdf.parse(startDate);
			date2 = sdf.parse(endDate);
			
			days = (date2.getTime() - date1.getTime()) / (24*3600*1000);
			yushu = (date2.getTime() - date1.getTime()) % (24*3600*1000);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return (int)days+1;
	}
	
	/**
	 * Method name: toLowCase <BR>
	 * Description: 第一个字母小写 <BR>
	 * Remark: <BR>
	 * @param s
	 * @return  String<BR>
	 */
	public static String toLowCase(String s) {
		return s.substring(0, 1).toLowerCase()+s.substring(1,s.length());
	}
	
	/**
	 * Method name: setStartUP <BR>
	 * Description: 第一个字母大写 <BR>
	 * Remark: <BR>
	 * @param s
	 * @return  String<BR>
	 */
	public static String setStartUP(String s) {
		return s.substring(0,1).toUpperCase()+s.substring(1,s.length());
	}
	
	/**
	 * Method name: getUp_ClassName <BR>
	 * Description: 根据表名获取类名不带后缀Bean <BR>
	 * Remark: <BR>
	 * @param s
	 * @return  String<BR>
	 */
	public static String getUp_ClassName(String s) {
		String cName = "";
		//首字母大写
		cName = s.substring(1,2).toUpperCase()+s.substring(2, s.length());
		
		String[] tem = cName.split("_");
		int len = tem.length;
		cName = tem[0];
		for (int i=1; i<len; i++) {		
			cName += setStartUP(tem[i]);
		}
		//tables.add(cName);//把所有的表添加到这里
		return cName;
	}
	
	/**
	 * Method name: getFiled2Pro <BR>
	 * Description: 根据字段名获取属性 <BR>
	 * Remark: <BR>
	 * @return  String<BR>
	 */
	public static String getFiled2Pro(String s) {
		String pName = "";
		String[] tem = s.split("_");
		
		int len = tem.length;
		pName = tem[0];
		for (int i=1; i<len; i++) {		
			pName += setStartUP(tem[i]);
		}
		return pName;
	}
	
	/**
	 * Method name: getLocalIp <BR>
	 * Description: 获取配置文件里面的ip地址 <BR>
	 * Remark: <BR>
	 * @return  String<BR>
	 */
	/*public static String getLocalIp() {
		File f2 = new File(MyUtils.class.getResource("").getPath());
		String path = f2.getAbsolutePath();
		File f = new File(path+"/localIp.txt");
		String ip= "127.0.0.1";
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			ip = br.readLine().trim();
			br.close();
		}catch (IOException e) {
			System.out.println("读取localIp文件错误");
			e.printStackTrace();
		}
		if(ip==null||ip.equals("")) {
			ip= "127.0.0.1";
		}
		return ip;
	}*/
	/**
	 * Method name: getLocalIp <BR>
	 * Description: 自动获取本地ip地址 <BR>
	 * Remark: <BR>
	 * @return  String<BR>
	 */
	public static String getLocalIp() {
		String hostAddress = "127.0.0.1";
		try {
		    InetAddress address = InetAddress.getLocalHost();//获取的是本地的IP地址 //PC-20140317PXKX/192.168.0.121
		    hostAddress = address.getHostAddress();//192.168.0.121
		    //InetAddress address1 = InetAddress.getByName("www.wodexiangce.cn");//获取的是该网站的ip地址，比如我们所有的请求都通过nginx的，所以这里获取到的其实是nginx服务器的IP地
		   // String hostAddress1 = address1.getHostAddress();//124.237.121.122
		    //InetAddress[] addresses = InetAddress.getAllByName("www.baidu.com");//根据主机名返回其可能的所有InetAddress对象
		   /* for(InetAddress addr:addresses){
		      System.out.println(addr);//www.baidu.com/14.215.177.38
		      //www.baidu.com/14.215.177.37
		    }
		    System.out.println(address+"\t-----");
		    System.out.println(hostAddress+"\t*****");
		    System.out.println(hostAddress1+"\t+++++++");*/
		  } catch (UnknownHostException e) {
			System.out.println("获取本地ip地址失败!");
		    e.printStackTrace();
		  }
		return hostAddress;
	}
	
	/**
	 * Method name: getNextDate <BR>
	 * Description: 返回当前日期的下一天 yyyy-MM-dd <BR>
	 * Remark: <BR>
	 * @return  String<BR>
	 */
	public static String getNextDate() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 1);
		return sf.format(c.getTime());
	}
	/**
	 * Method name: String2Date <BR>
	 * Description: 将字符串日期转换成日期类型  yyyy-MM-dd <BR>
	 * Remark: <BR>
	 * @return  Date<BR>
	 */
	public static Date String2Date(String date) {
		Date d = new Date();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
			d = sdf.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return d;
	}
	
}
