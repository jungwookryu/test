package com.ht.connected.home.backend.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HashConvert {

	final static String expectSHA = "e150a1ec81e8e93e1eae2c3a77e66ec6dbd6a3b460f89c1d08aecf422ee401a0";

	@Test
	public void testSHA256(){

		final String str = "123456Test";
		/**
		 * algorithm
		 * <ul>
		 * <li>{@code MD5}</li>
		 * <li>{@code SHA-1}</li>
		 * <li>{@code MessageDigestAlgorithms.SHA_256}</li>
		 * </ul>
		 * **/
		String SHA = ""; 
		String algorithm =MessageDigestAlgorithms.SHA_256;
		try{

			MessageDigest sh = MessageDigest.getInstance(algorithm); 

			sh.update(str.getBytes()); 

			byte byteData[] = sh.digest();

			StringBuffer sb = new StringBuffer(); 

			for(int i = 0 ; i < byteData.length ; i++){

				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));

			}

			SHA = sb.toString();

			

		}catch(NoSuchAlgorithmException e){

			e.printStackTrace(); 

			SHA = null; 

		}
		  Assert.assertEquals(expectSHA,SHA);

	}

}
