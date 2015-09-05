package com.hplans.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.hplans.po.BaseMessage;
import com.hplans.po.Image;
import com.hplans.po.ImageMessage;
import com.hplans.po.TextMessage;
import com.thoughtworks.xstream.XStream;

public class Message {
	
	public static final String MESSAGE_TEXT = "text";
	public static final String MESSAGE_IMAGE = "image";
	public static final String MESSAGE_VOICE = "voice";
	public static final String MESSAGE_VIDEO = "video";
	public static final String MESSAGE_LINK = "link";
	public static final String MESSAGE_LOCALTION = "location";
	public static final String MESSAGE_EVENT = "event";
	public static final String MESSAGE_SUBSCRIBE = "subscribe";
	public static final String MESSAGE_UBSUBSCRIBE = "unsubscribe";
	public static final String MESSAGE_CLICK = "CLICK";
	public static final String MESSAGE_VIEW = "VIEW";
	
	
	public static Map<String, String > xmlToMap(HttpServletRequest request) throws IOException, DocumentException{
		Map<String, String> map = new HashMap<String, String>();		
		SAXReader reader = new SAXReader();
		
		InputStream ins = request.getInputStream();
		Document doc = reader.read(ins);
		
		Element root = doc.getRootElement();
		
		List<Element> list = root.elements();
		
		for(Element e : list){
			map.put(e.getName(), e.getText());
		}
		ins.close();
		return map;
	}
	
	
	
	private static String MessageToXml(BaseMessage message) {
		XStream xstream = new XStream();
		xstream.alias("xml", message.getClass());
		return xstream.toXML(message);
	}
	
	public static String initText(String toUserName, String fromUserName, String content){
		TextMessage text = new TextMessage();
		text.setFromUserName( toUserName);
		text.setToUserName(fromUserName);
		text.setMsgType(Message.MESSAGE_TEXT);
		text.setCreateTime(new Date().getTime());
		text.setContent("您发送的消息是："+ content);
		return MessageToXml(text);
	}
	
	

	public static String initImage(String toUserName, String fromUserName, String mediaId){
		ImageMessage image = new ImageMessage();
		image.setFromUserName( toUserName);
		image.setToUserName(fromUserName);
		image.setMsgType(Message.MESSAGE_IMAGE);
		image.setCreateTime(new Date().getTime());
		Image Image = new Image();
		Image.setMediaId(mediaId);
		image.setImage(Image);
		return MessageToXml(image);
	}
	
	
	public static String menuText(){
		StringBuffer sb = new StringBuffer();
		sb.append("欢迎您的关注，请按照菜单提示进行操作：\n\n");
		sb.append("1、彭辛航介绍\n");
		sb.append("2、关于彭辛航\n\n");
		sb.append("回复？调出此菜单");
		return sb.toString();
	}
	
	public static String firstMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("关于彭辛航：\n\n");
		return sb.toString();
	}
	
	public static String secondMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("彭辛航的简历：\n\n");
		return sb.toString();
	}
}
