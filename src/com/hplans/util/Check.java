package com.hplans.util;

import java.util.Arrays;

import com.hplans.base.Consts;

public class Check {
	private static final String token = Consts.token;
	public static boolean checkSignature(String signature, String timestamp, String nonce){
		String[] arr = new String[]{token, timestamp, nonce};
		
		//排序
		Arrays.sort(arr);
		
		//生成字符创
		StringBuffer content = new StringBuffer();
		for(int i=0; i<arr.length; i++){
			content.append(arr[i]);
		}
		
		//sha1加密
		String temp = SHA.getSha1(content.toString());
		
		return temp.equals(signature);
	}
}
