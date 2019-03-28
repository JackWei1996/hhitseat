/*
 * All rights Reserved, Copyright (C) Aisino LIMITED 2018
 * FileName: WebConfig.java
 * Version:  $Revision$
 * Modify record:
 * NO. |     Date       |    Name         |      Content
 * 1   | 2019年1月16日        | Aisino)Jack    | original version
 */
package com.jack.hhitseat.controller;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jack.hhitseat.model.ResultMap;


/**
 * class name:LoginController <BR>
 * class description: 登录操作 <BR>
 * Remark: <BR>
 * @version 1.00 2019年1月16日
 * @author Aisino)weihaohao
 */
@Controller
public class LoginController {
	@Autowired
    private ResultMap resultMap;

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);
    
    @RequestMapping(value = "/ct", method = RequestMethod.GET)
    public String ct() {
    	return "ct";
    }
    @RequestMapping(value = "/zz")
    public String zz(String ct,Model model, String p, String u, String zw) {
    	
    	String VIEWSTATE = "/wEPDwUJNjU0ODExMzM0ZGRgUvbey+950Xh7OEw/3GjPUybDryxCZIHQ5K66jJE7yw==";
    	String EVENTVALIDATION = "/wEWBALjq7r5AQL36ofzAQKLh62dDwLE8+zdCFZDURdcT2Kt0SYlY4v4gQpOiMa+muRIorC4AKL3mnAV";
    	try {
    		
    		URL url=new URL("http://seat.hhit.edu.cn/pages/ic/LoginForm.aspx");
    		URLConnection uc=url.openConnection();
    		InputStream raw=uc.getInputStream();
    		InputStream buffer=new BufferedInputStream(raw);
    		
    		Reader r=new InputStreamReader(buffer);
    		
    		String src="";
            int c;
            while((c=r.read())!=-1){
                  src += (char)c;
           	}
            String[] tt1 = src.split("id=\"__VIEWSTATE\" value=\"");
            String[] tt2 = tt1[1].split("\" />");
            VIEWSTATE = tt2[0];
            
            String[] tt3 = src.split("id=\"__EVENTVALIDATION\" value=\"");
            String[] tt4 = tt3[1].split("\" />");
            EVENTVALIDATION = tt4[0];
    	}catch (Exception e) {
			logger.error("解析登录网址异常",e);
		}
    	model.addAttribute("VIEWSTATE",VIEWSTATE);
    	model.addAttribute("EVENTVALIDATION",EVENTVALIDATION);
    	
    	model.addAttribute("ct", ct);
    	model.addAttribute("p", p);
    	model.addAttribute("u", u);
    	model.addAttribute("zw", zw);
    	return "zz";
    }

    @RequestMapping(value = "/notLogin", method = RequestMethod.GET)
    @ResponseBody
    public ResultMap notLogin() {
    	logger.warn("尚未登陆！");
        return resultMap.success().message("您尚未登陆！");
    }

    @RequestMapping(value = "/notRole", method = RequestMethod.GET)
    @ResponseBody
    public ResultMap notRole() {
    	logger.warn("没有权限！");
        return resultMap.success().message("您没有权限！");
    }

    /**
     * Method name: logout <BR>
     * Description: 退出登录 <BR>
     * @return  String<BR>
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        return "login";
    }
    
    /**
     * Method name: index <BR>
     * Description: 登录页面 <BR>
     * @return  String login.html<BR>
     */
    @RequestMapping(value = "/")
    public String index() {
    	return "login";
    }
    
    
    @RequestMapping(value = "/tolog")
    @ResponseBody
    public String tolog(String ip, String cname, String op) {
    	logger.warn("{}---[{}]---[{}]",op, ip, cname);
    	return "-.-";
    }
    
    @RequestMapping(value = "/jg")
    @ResponseBody
    public String jg(String url) {
    	logger.error("url----：{}",url);
    	String src="";
    	try {
    		URL u=new URL(url);
    		URLConnection uc=u.openConnection();
    		InputStream raw=uc.getInputStream();
    		InputStream buffer=new BufferedInputStream(raw);
    		
    		Reader r=new InputStreamReader(buffer);

            int c;
            while((c=r.read())!=-1){
                  src += (char)c;
           	}
    	}catch (Exception e) {
    		logger.error("解析登录网址错误：{}",src);
		}
    	logger.error("结果是：{}",src);
    	return src;
    }
    
    @RequestMapping(value = "/tt")
    public String tt(String ct,Model model, 
    		String p, String u, String zw,
    		String H, String M, String S) {
    	
    	String VIEWSTATE = "/wEPDwUJNjU0ODExMzM0ZGRgUvbey+950Xh7OEw/3GjPUybDryxCZIHQ5K66jJE7yw==";
    	String EVENTVALIDATION = "/wEWBALjq7r5AQL36ofzAQKLh62dDwLE8+zdCFZDURdcT2Kt0SYlY4v4gQpOiMa+muRIorC4AKL3mnAV";
    	try {
    		
    		URL url=new URL("http://seat.hhit.edu.cn/pages/ic/LoginForm.aspx");
    		URLConnection uc=url.openConnection();
    		InputStream raw=uc.getInputStream();
    		InputStream buffer=new BufferedInputStream(raw);
    		
    		Reader r=new InputStreamReader(buffer);
    		
    		String src="";
            int c;
            while((c=r.read())!=-1){
                  src += (char)c;
           	}
            String[] tt1 = src.split("id=\"__VIEWSTATE\" value=\"");
            String[] tt2 = tt1[1].split("\" />");
            VIEWSTATE = tt2[0];
            
            String[] tt3 = src.split("id=\"__EVENTVALIDATION\" value=\"");
            String[] tt4 = tt3[1].split("\" />");
            EVENTVALIDATION = tt4[0];
    	}catch (Exception e) {
			logger.error("访问zz异常，登录网址解析错误",e);
		}
    	model.addAttribute("VIEWSTATE",VIEWSTATE);
    	model.addAttribute("EVENTVALIDATION",EVENTVALIDATION);
    	
    	model.addAttribute("ct", ct);
    	model.addAttribute("p", p);
    	model.addAttribute("u", u);
    	model.addAttribute("zw", zw);
    	model.addAttribute("H", H);
    	model.addAttribute("M", M);
    	model.addAttribute("S", S);
    	return "tt";
    }
    //生成座位
    public void sczw(int id, int n,String js) {
    	/*int id = 100455668;
    	int n = 360;
    	String js = "西104-";*/
    	for (int i = 1; i <= n; i++) {
			if(i<10) {
				System.out.println("<option value=\""+(id++)+"\">"+js+"00"+i+"</option>");
			}else if(i<100){
				System.out.println("<option value=\""+(id++)+"\">"+js+"0"+i+"</option>");
			}else {
				System.out.println("<option value=\""+(id++)+"\">"+js+""+i+"</option>");
			}
		}
    }
    
    @RequestMapping(value = "/jz")
    public String jz(Model model) {
    	return "jz";
    }
}
