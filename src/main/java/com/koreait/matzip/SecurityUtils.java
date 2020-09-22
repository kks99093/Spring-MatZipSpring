package com.koreait.matzip;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

import com.koreait.matzip.user.model.UserVO;


public class SecurityUtils {
	
	//세션에서 I_user 불러오는 메소드를 따로 만듬
	public static int getLoginUserPk(HttpServletRequest request) {
		return getLoginUser(request).getI_user();
	}
	
	public static int getLoginUserPk(HttpSession hs) {
		return ((UserVO)hs.getAttribute(Const.LOGIN_USER)).getI_user();
	}
//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ로그인 판단ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	
	public static UserVO getLoginUser(HttpServletRequest request) {
		HttpSession hs = request.getSession();
		return (UserVO) hs.getAttribute(Const.LOGIN_USER);
		//UserVO가 UserPARAM의 부모이기떄문에 UserVO로 받아도 괜춘
	}

	public static boolean isLogout(HttpServletRequest request) {
		return getLoginUser(request) == null;
	}

	
//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ비밀번호 암호화 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//라이브러리 사용한것
	public static String generateSalt() {
		return BCrypt.gensalt();
	}
	
	public static String getEncrypt(String pw, String salt) {
		return BCrypt.hashpw(pw, salt);
	}

}
