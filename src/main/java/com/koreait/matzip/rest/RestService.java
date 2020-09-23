package com.koreait.matzip.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public List<CodeVO> selCategoryList(){
		CodeVO p = new CodeVO();
		p.setI_m(1); //음식점 카테고리 코드 : 1
		return cMapper.selCodeList(p);
	}
	
	// 디테일 셀렉트
	public RestDMI selRest(RestPARAM param) {
		return mapper.selRest(param);
	}
	
	//트랜잭션으로 레스토랑 삭제
	@Transactional
	public void delRestTran(RestPARAM param) {
		mapper.delRestRecMenu(param);
		mapper.delRestMenu(param);
		mapper.delRest(param);
		//여기서 직접 mapper 호출해야함
		//여기서 밑의 메소드를 호출한다면 Transaction이 걸리지 않음
	}
	//추천메뉴 삭제
	public int delRestRecMenu(RestPARAM param) {
		return mapper.delRestRecMenu(param);
	}
	//메뉴삭제
	public int delRestMenu(RestPARAM param) {
		return mapper.delRestMenu(param);
	}
}
