package com.koreait.matzip.user;

import org.apache.ibatis.annotations.Mapper;

import com.koreait.matzip.user.model.UserDMI;
import com.koreait.matzip.user.model.UserPARAM;
import com.koreait.matzip.user.model.UserVO;

@Mapper // 마이바티스가 import됨, 마이바티스가 Mapper가 있는지 찾는것
public interface UserMapper {
	public int insUser(UserVO p);
	public UserDMI selUser(UserPARAM p);

}
