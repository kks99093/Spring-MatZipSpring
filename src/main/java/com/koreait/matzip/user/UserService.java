package com.koreait.matzip.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.koreait.matzip.Const;
import com.koreait.matzip.SecurityUtils;
import com.koreait.matzip.user.model.UserDMI;
import com.koreait.matzip.user.model.UserPARAM;
import com.koreait.matzip.user.model.UserVO;

@Service //컴포넌트랑 똑같이생각하면 됨
public class UserService {
	@Autowired
	private UserMapper mapper;

//로그인 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	//1번 로그인성공, 2번 아이디없음, 3번 비번틀림
	public int login(UserPARAM param) {
		if(param.getUser_id().equals("")) { return Const.NO_ID; }
		UserDMI dbUser =mapper.selUser(param);

		if(dbUser == null) { return Const.NO_ID;	}
		
		String crypPw = SecurityUtils.getEncrypt(param.getUser_pw(), dbUser.getSalt());		
		if(!dbUser.getUser_pw().equals(crypPw)) {
			return Const.NO_PW;		
		}
		
		param.setUser_pw(null);
		param.setI_user(dbUser.getI_user());
		param.setNm(dbUser.getNm());
		param.setProfile_img(dbUser.getProfile_img());
		
		return Const.SUCCESS;		
		
		
	}
	
//회원가입 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ	
	public int join(UserVO param) {
		String pw = param.getUser_pw();
		String salt = SecurityUtils.generateSalt();
		String cryptPw = SecurityUtils.getEncrypt(pw, salt);
		
		param.setSalt(salt);
		param.setUser_pw(cryptPw);

		return mapper.insUser(param);
		//nm이길거나 아이디가 길면 에러가 터지는데 그럴경우 아에 여기로 안넘어오게 자바스크립트에서 길이체크를 해주자
	}

}
