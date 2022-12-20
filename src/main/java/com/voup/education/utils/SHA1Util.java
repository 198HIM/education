package com.voup.education.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;


//SHA-1
public class SHA1Util {
    public static String sha1(String psw) {
        if(StringUtils.isEmpty(psw)){
            return null;
        }else{
        	
            return DigestUtils.sha1Hex(psw);
        }
    } 
    
    public static String shaEncode(String inStr) throws Exception {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = inStr.getBytes("UTF-8");
        byte[] md5Bytes = sha.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
    
 
  	public static String getSha1(String str){
  		if(str == null || str.length() == 0){
  			return null;
  		}
  		char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
  		
  		try {
  			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
  			mdTemp.update(str.getBytes("UTF-8"));
  			
  			byte[] md = mdTemp.digest();
  			int j = md.length;
  			char buf[] = new char[j*2];
  			int k = 0;
  			for(int i =0;i<j;i++){
  				byte byteO = md[i];
  				buf[k++] = hexDigits[byteO >>> 4 & 0xf];
  				buf[k++] = hexDigits[byteO & 0xf];
  			}
  			return new String(buf);
  		} catch (NoSuchAlgorithmException e) {
  			return null;
  		} catch (UnsupportedEncodingException e) {
  			return null;
  		}
  	}

  
    public static void main(String[] args) {
    	
		String demo="I hava a dream";
		byte [] bytes=demo.getBytes();
		
		System.out.println(sha1(demo));
		try {
			System.out.println(shaEncode(demo));
			System.out.println(getSha1(demo));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

