package com.koreait.matzip.rest;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestPARAM;

@Mapper
public interface RestMapper {
	public List<RestDMI> selRestList(RestPARAM param);
	//List라도 그냥 Select하면 다 받아짐
	
	public int insReg(RestPARAM param);

}
