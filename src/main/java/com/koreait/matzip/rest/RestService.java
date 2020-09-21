package com.koreait.matzip.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestPARAM;

@Service
public class RestService {
	
	@Autowired
	private RestMapper mapper;

	public String selRestList(RestPARAM param){
		List<RestDMI> list = mapper.selRestList(param);
		//없으면 null이 아니라 빈칸이 넘어옴으로 그냥 넘겨도됨
		Gson gson = new Gson();
		return gson.toJson(list);
	}
	
	public int insReg(RestPARAM param) {
		return mapper.insReg(param);
	}
}
