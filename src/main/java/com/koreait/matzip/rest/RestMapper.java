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
	RestDMI selRest(RestPARAM param);
	List<RestRecMenuVO> selRestRecMenus(RestPARAM param);
	List<RestRecMenuVO> selRestMenus(RestPARAM param);
	int selRestChkUser(int param);
	
	int insRest(RestPARAM param);
	int insRestRegMenu(RestRecMenuVO param);
	int insMenus(RestRecMenuVO param);
	
	
	int delRestRecMenu(RestPARAM param);
	int delRestMenu(RestPARAM param);
	int delRest(RestPARAM param);
	
	
	int updAddHits(RestPARAM param);
	
	
	
	
	
}
