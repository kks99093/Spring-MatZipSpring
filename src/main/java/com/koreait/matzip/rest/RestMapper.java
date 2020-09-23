package com.koreait.matzip.rest;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestPARAM;
import com.koreait.matzip.rest.model.RestRecMenuVO;

@Mapper
public interface RestMapper {
	//인터페이스는 앞에 자동으로 public abstruct가 생략되어있기에 public 안붙엳됨
	List<RestDMI> selRestList(RestPARAM param);
	//List라도 그냥 Select하면 다 받아짐
	
	int insRest(RestPARAM param);
	
	RestDMI selRest(RestPARAM param);
	
	int delRestRecMenu(RestPARAM param);
	int delRestMenu(RestPARAM param);
	int delRest(RestPARAM param);
	
	void insRestRegMenu(RestRecMenuVO param);
	List<RestRecMenuVO> selRestRecMenus(RestPARAM param);
	
	
}
