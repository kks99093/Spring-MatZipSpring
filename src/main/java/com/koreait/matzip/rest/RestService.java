package com.koreait.matzip.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.koreait.matzip.model.CodeVO;
import com.koreait.matzip.model.CommonMapper;
import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestPARAM;

@Service
public class RestService {

	@Autowired
	private RestMapper mapper;
	
	@Autowired
	private CommonMapper cMapper;

	
	public List<RestDMI> selRestList(RestPARAM param) {
		
		return mapper.selRestList(param);
	}

	//가게 등록
	public int insRest(RestPARAM param) {
		return mapper.insRest(param);
	}
	
	//코드 불러오기
	List<CodeVO> selCategoryList(){
		CodeVO p = new CodeVO();
		p.setI_m(1); //음식점 카테고리 코드 : 1
		return cMapper.selCodeList(p);
	}
	
	// 디테일 셀렉트
	RestDMI selRest(RestPARAM param) {
		return mapper.selRest(param);
	}
}
