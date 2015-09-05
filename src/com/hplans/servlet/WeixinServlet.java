package com.hplans.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.ParseException;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hplans.base.Consts;
import com.hplans.po.AccessToken;
import com.hplans.util.Check;
import com.hplans.util.Message;
import com.hplans.util.TokenThread;
import com.hplans.util.Weixin;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class WeixinServlet
 */
public class WeixinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(WeixinServlet.class); 
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WeixinServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    public void init() throws ServletException {    
    	log.info("weixin api appid:{}", Consts.AppID);    
    	log.info("weixin api appsecret:{}", Consts.AppSecret);    
    	// 未配置appid、appsecret时给出提示    
    	if ("".equals(Consts.AppID) || "".equals(Consts.AppSecret)) {    
    		log.error("appid and appsecret configuration error, please check carefully.");    
    	} else {    
    	// 启动定时获取access_token的线程    
    		new Thread(new TokenThread()).start(); 
    		//开启时先初始化一下菜单
    		String menu = JSONObject.fromObject(Weixin.initMenu()).toString();
			try {
				Weixin.createMenu(Weixin.getAccessToken().getToken(), menu);
			} catch (ParseException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}    
    } 


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		
		PrintWriter out = response.getWriter();
		
		if(Check.checkSignature(signature, timestamp, nonce)){
			out.println(echostr);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			Map<String, String> map = Message.xmlToMap(request);
		
			String toUserName = map.get("ToUserName");
			String fromUserName = map.get("FromUserName");
			String msgType = map.get("MsgType");
			String content = map.get("Content");
	
			String message = null;
			if(Message.MESSAGE_TEXT.equals(msgType)){
				if("1".equals(content)){
					message = Message.initText(toUserName, fromUserName, Message.firstMenu());
				}else if("2".equals(content)){
					message = Message.initText(toUserName, fromUserName, Message.secondMenu());
				}else if("?".equals(content) || "？".equals(content)){
					message = Message.initText(toUserName, fromUserName, content);
				}
				
//				TextMessage text = new TextMessage();
//				text.setFromUserName( toUserName);
//				text.setToUserName(fromUserName);
//				text.setMsgType("text");
//				text.setCreateTime(new Date().getTime());
//				text.setContent("您发送的消息是："+ content);
//				message = Message.textMessageToXml(text);
			}else if(Message.MESSAGE_EVENT.equals(msgType)){
				String eventType = map.get("Event");
				if(Message.MESSAGE_SUBSCRIBE.equals(eventType)){
					message = Message.initText(toUserName, fromUserName, Message.menuText());
				}else if(Message.MESSAGE_CLICK.equals(eventType)){
					String eventKey = map.get("EventKey");
					if(Consts.pxhjj.equals(eventKey)){
						//这里的mediaId是我一开始上传好东西写死的
						message = Message.initImage(toUserName, fromUserName, "fbT46okugBm5F35LCZcEWJGqBqw9hFnLzXdynoG95fXrFHeSHHswSvtXbgDov6Gf");
						System.out.println(message);
					}
					
				}
			}
			
			out.println(message);
			
		
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			out.close();
		}
	}

}
