package com.hplans.test;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import org.apache.http.ParseException;
import org.junit.Test;

import com.hplans.po.AccessToken;
import com.hplans.util.Weixin;

import net.sf.json.JSONObject;

public class WeixinTest {
	public static void main(String[] args) {
		try {
			AccessToken token = Weixin.getAccessToken();
			System.out.println("票据"+token.getToken());
			System.out.println("有效时间"+token.getExpiresIn());
			
			
			String menu = JSONObject.fromObject(Weixin.initMenu()).toString();
			int result = Weixin.createMenu(token.getToken(), menu);
			if(result==0){
				System.out.println("创建菜单成功");
			}else{
				System.out.println("错误码："+result);
			}
			
			//String result = Weixin.translate("my name is 彭辛航");
			//String result = WeixinUtil.translateFull("");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void scyjxc() throws ParseException, IOException, KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException{
		AccessToken token = Weixin.getAccessToken();
		String path = "G:/Michael_QRCode.png";
		String mediaId = Weixin.upload(path, token.getToken(), "image");
		System.out.println(mediaId);
	}
}
