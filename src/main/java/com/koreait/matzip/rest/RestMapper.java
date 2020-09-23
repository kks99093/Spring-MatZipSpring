package com.koreait.matzip.rest;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestPARAM;

@Mapper
public interface RestMapper {
	List<RestDMI> selRestList(RestPARAM param);
	//List라도 그냥 Select하면 다 받아짐
	//인터페이스라 접근제어자(public)굳이 안써도됨
	
	int insRest(RestPARAM param);
	
	RestDMI selRest(RestPARAM param);
	
	int delRestRecMenu(RestPARAM param);
	int delRestMenu(RestPARAM param);
	int delRest(RestPARAM param);
	
	
}
