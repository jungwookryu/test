package com.ht.connected.home.backend.common;

import com.ht.connected.home.backend.config.service.EmailConfig;
import com.ht.connected.home.backend.model.entity.Users;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author ijlee
 *         <p>
 *         create by 2017.06.21
 */
public class Common {

	private static String iv;
	private static Key keySpec;
	private static Logger logger = LoggerFactory.getLogger(Common.class);
	
	/**
	 * 날짜계산
	 * 
	 * @return string string
	 */

	/*
	 * @Autowired public MessageSource messageSource;
	 */
	public static int diffDay(Date d, Date accessDate) {
		Calendar curC = Calendar.getInstance();
		Calendar accessC = Calendar.getInstance();
		curC.setTime(d);
		accessC.setTime(accessDate);
		accessC.compareTo(curC);
		int diffCnt = 0;
		while (!accessC.after(curC)) {
			diffCnt++;
			accessC.add(Calendar.DATE, 1); // 다음날로 바뀜
		}
		return diffCnt;
	}

	/**
	 * 요청 파라미터들의 빈값 또는 null값 확인을 하나의 메소드로 처리할 수 있도록 생성한 메소드 요청 파라미터 중 빈값 또는 null값인
	 * 파라미터가 있는 경우, false를 리턴한다. * @param params
	 * 
	 * @return
	 */
	public static boolean stringNullCheck(String... params) {
		return Arrays.stream(params).allMatch(param -> null != param && !param.equals(""));
	}

	/**
	 * Object type 변수가 비어있는지 체크
	 *
	 * @param obj
	 * @return Boolean : true / false Create by injeong
	 */
	public static Boolean empty(Object obj) {
		if (obj instanceof String) {
			return StringUtils.isEmpty(obj.toString().trim());
		} else if (obj instanceof List) {
			return ((List) obj).isEmpty();
		} else if (obj instanceof Map) {
			return ((Map) obj).isEmpty();
		} else if (obj instanceof Object[]) {
			return Array.getLength(obj) == 0;
		} else {
			return obj == null;
		}
	}

	/**
	 * Object type 변수가 비어있지 않은지 체크
	 *
	 * @param obj
	 * @return Boolean : true / false
	 */
	public static Boolean notEmpty(Object obj) {
		return !Common.empty(obj);
	}

	/**
	 * Object type 변수가 비어있는지 확인하여 비어있으면 rtn값을 주고 비어있지 않으면 Object 를 보내준다.
	 *
	 * @param obj
	 *            확인할 변수
	 * @param rtn
	 *            비어있으면 보내줄값 Create by injeong
	 */

	public static Object notNullrtnByobj(Object obj, Object rtn) {
		if (Common.empty(obj)) {
			return rtn;
		} else {
			return obj;
		}
	}

	@SuppressWarnings("unchecked")
	public static Map rtnMapByRequestParam(HttpServletRequest request) {
		Map parameterMap = new HashMap();
		Enumeration enumeration = request.getParameterNames();
		if (enumeration == null) {
			return parameterMap;
		}
		while (enumeration.hasMoreElements()) {
			String paramName = (String) enumeration.nextElement();
			String[] parameters = request.getParameterValues(paramName);

			// Parameter가 배열일 경우
			if (parameters.length > 1) {
				List<Object> parmList = new ArrayList<>();

				for (String parameter : parameters) {
					parmList.add(parmList.size(), parameter);
				}
				parameterMap.put(paramName, parmList);
				// Parameter가 배열이 아닌 경우
			} else {
				parameterMap.put(paramName, parameters[0]);
			}
		}

		return parameterMap;
	}

	/**
	 * 사용자 이름, 사용자 list 를 가져와서 사용자 정보를 return해준다. 전체 사용자 조회를 하여 사용자 정보 를 가져와야 할 경우
	 * 사용.
	 */
	public static Users getUserByUserEmail(String userEmail, List<Users> lstUser) {
		for (Users aLstUser : lstUser) {
			if (userEmail.equals(aLstUser.getUserEmail())) {
				return aLstUser;
			}
		}
		Users emptyUser = new Users();
		return emptyUser;
	}

	/**
	 * 사용자 아이디 또는 이름 검색을 위한 List 필터
	 */
	@SuppressWarnings("unchecked")
	public static List<Users> fillterUser(String userEmail, List<Users> list) {
		List rtnList = new ArrayList();
		list.forEach(user -> {
			if (Common.empty(userEmail)) {
				list.add(user);
			}
			if (Common.notEmpty(userEmail)) {
				if (user.getUserEmail().contains(userEmail)) {
					list.add(user);
				}
			}
		});
		return rtnList;
	}

	@SuppressWarnings("unchecked")
	public static Map convertMapByLinkedHashMap(LinkedHashMap linkedHashMap) {
		Map map = new HashMap();
		if (!Common.empty(linkedHashMap)) {
			List<String> lstKey = new ArrayList(linkedHashMap.keySet());
			List<Object[]> lstValue = new ArrayList(linkedHashMap.values());
			for (int i = 0; i < lstKey.size(); i++) {
				map.put(lstKey.get(i), lstValue.get(i));
			}

		}
		return map;
	}

	public static Map convertMapByRequest(HttpServletRequest request) {

		Map requestParameterMap = request.getParameterMap();
		Map map = new HashMap();
		if (!Common.empty(requestParameterMap)) {
			List<String> lstKey = new ArrayList(requestParameterMap.keySet());
			List<Object[]> lstValue = new ArrayList(requestParameterMap.values());
			for (int i = 0; i < lstKey.size(); i++) {
				map.put(lstKey.get(i), lstValue.get(i)[0]);
			}

		}
		return map;

	}

	/**
	 * 16자리의 키값을 입력하여 객체를 생성한다.
	 * 
	 * @param key
	 *            암/복호화를 위한 키값
	 * @return
	 * @throws UnsupportedEncodingException
	 *             키값의 길이가 16이하일 경우 발생
	 */
	public void aES256Util(String key) throws UnsupportedEncodingException {
		this.iv = key.substring(0, 16);
		byte[] keyBytes = new byte[16];
		byte[] b = key.getBytes("UTF-8");
		int len = b.length;
		if (len > keyBytes.length) {
			len = keyBytes.length;
		}
		System.arraycopy(b, 0, keyBytes, 0, len);
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

		this.keySpec = keySpec;
	}

	/**
	 * AES256 으로 암호화 한다.
	 * 
	 * @param str
	 *            암호화할 문자열
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws GeneralSecurityException
	 * @throws UnsupportedEncodingException
	 */
	public static String encrypt(String str)
			throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException {
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
		byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
		String enStr = new String(Base64.encodeBase64(encrypted));
		return enStr;
	}

	/**
	 * AES256으로 암호화된 txt 를 복호화한다.
	 * 
	 * @param str
	 *            복호화할 문자열
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws GeneralSecurityException
	 * @throws UnsupportedEncodingException
	 */
	public static String decrypt(String str)
			throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException {
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
		byte[] byteStr = Base64.decodeBase64(str.getBytes());
		return new String(c.doFinal(byteStr), "UTF-8");
	}

	/**
	 * 암호화
	 * 
	 * @param str
	 *            SHA-256 ,MD5, SH1
	 * @param password
	 *            password
	 * @return 암호화 문자열
	 * @throws NoSuchAlgorithmException
	 */
	public static String encryptHash(String sSHA, String password) {

		String encryptPassword = "";
		logger.debug("password::::::::::::"+password);
		try {
			MessageDigest sh = MessageDigest.getInstance(sSHA);
			logger.debug("sh::::::::::::"+sh);
			sh.update(password.getBytes());
			
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			encryptPassword = sb.toString();
			return encryptPassword;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

	}
/**
 * 
 * @param body : Map User "rtnUsers"
 * @return
 * @throws IOException
 * @throws MessagingException
 */
	public static boolean sendEmail(Map body, EmailConfig emailConfig, InputStream inputStremaFile) {
		Boolean bRtn = false;
		try {
			
			Properties properties = emailConfig.properties();
			
			/** 사용자 이메일**/
			Users users = (Users) body.getOrDefault("rtnUsers", new Users());
			String receiveUserEmail = users.getUserEmail();
			String redirectied_code = users.getRedirectiedCode();

			String username = (String) body.getOrDefault("username", properties.get("mail.smtp.username"));
			String userEmail = (String) body.getOrDefault("userEmail", properties.get("mail.smtp.userEmail"));
//			String sFile = (String) body.getOrDefault("sFile", properties.get("mail.smtp.sFile"));
			String activeUrl = (String) body.getOrDefault("activeUrl", properties.get("mail.smtp.active.authUrl"));
			String authUrl = (String) body.getOrDefault("authUrl", properties.get("mail.smtp.authUrl"));
			String subject = (String) body.getOrDefault("subject", properties.get("mail.smtp.subject"));
			String contextUrl = (String) body.getOrDefault("contextUrl", properties.get("mail.smtp.contextUrl"));
			Session session = Session.getInstance(properties, emailConfig.auth());
			
			Document doc = Jsoup.parse(inputStremaFile, "UTF-8", "./");
			Elements elementAhref = doc.select("a[href]");
			Elements elementSpan = doc.select("span");

			if (elementAhref.size() != 0) {
				elementAhref.get(0).attr("href",
						activeUrl + authUrl + "/" + contextUrl + "?user_email=" + receiveUserEmail + "&redirected_code=" + redirectied_code);
			}
			if (elementSpan.size() != 0) {
				elementSpan.get(0).childNode(0).attr("text", receiveUserEmail);
			}

			MimeMessage msg = new MimeMessage(session);
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setDataHandler(new DataHandler(new ByteArrayDataSource(doc.outerHtml(), "text/html")));
			msg.setSentDate(new Date());
			msg.setSubject(subject);
			msg.setContent(doc.outerHtml(), "text/html;charset=" + "EUC-KR");
			msg.setFrom(new InternetAddress(userEmail, username));
			msg.setReplyTo(InternetAddress.parse(userEmail, false));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiveUserEmail, false));
			Transport.send(msg);
			bRtn = true;
			logger.info("sendEmail::"+userEmail+"::sucess");
		} catch (IOException | MessagingException e) {
			e.printStackTrace();

		}
		return bRtn;

	}
	
	public String randomCode() {
		String rtnRandomeCode="";
		rtnRandomeCode = RandomStringUtils.randomAlphanumeric(12).toUpperCase() + RandomStringUtils.randomAlphanumeric(7).toUpperCase();
		return rtnRandomeCode;
		
	}
	

}
