package com.koreait.matzip.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.koreait.matzip.CommonUtils;
import com.koreait.matzip.FileUtils;
import com.koreait.matzip.model.CodeVO;
import com.koreait.matzip.model.CommonMapper;
import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestPARAM;
import com.koreait.matzip.rest.model.RestRecMenuVO;

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
	// 추천메뉴 셀렉트
	public List<RestRecMenuVO> selResMenus(RestPARAM param) {
		return mapper.selRestRecMenus(param);
	}
	
	//트랜잭션으로 레스토랑 삭제
	@Transactional
	public void delRestTran(RestPARAM param) {
		mapper.delRestRecMenu(param);
		mapper.delRestMenu(param);
		mapper.delRest(param);
		//밑의 메소드를 호출해도됨
	}
	//추천메뉴 삭제
	public int delRestRecMenu(RestPARAM param) {
		return mapper.delRestRecMenu(param);
	}
	//메뉴삭제
	public int delRestMenu(RestPARAM param) {
		return mapper.delRestMenu(param);
	}
	
	//추천메뉴 등록(업로드)
	public int insResMenus(MultipartHttpServletRequest mReq) {
		
		int i_rest = Integer.parseInt(mReq.getParameter("i_rest"));
		List<MultipartFile> fileList = mReq.getFiles("menu_pic"); //이미지파일을 담는 List(배열생성) 
		String[] menuNmArr = mReq.getParameterValues("menu_nm"); //메뉴이름을 담는 String배열
		String[] menuPriceArr = mReq.getParameterValues("menu_price"); //가격을 담는 String 배열
		
		String path = mReq.getServletContext().getRealPath("/resources/img/rest/" + i_rest + "/rec_menu/");
		//저장위치 얻어오기(드라이브~프로젝트작업공간까지의 절대주소), pome.xml에서 서블릿,jsp를 최신버전으로 바꿔줘야함
		System.out.println(path);
		List<RestRecMenuVO> list = new ArrayList();
		
		for(int i=0; i<menuNmArr.length; i++) {
			RestRecMenuVO vo = new RestRecMenuVO();
			list.add(vo);
			//mf.isEmpty를 만나면 스킵되서 nm price도 안들어 가기때문에 여기서 먼저 list에 add함
			//list에 add를 한후 밑에서 값을set함(어처피 주소값으로 들어가는거라 여기서 넣든 마지막에 넣든 상관없음)			
			String menu_nm = menuNmArr[i];
			int menu_price = CommonUtils.parseStrToInt(menuPriceArr[i]);
			vo.setMenu_nm(menu_nm);
			vo.setMenu_price(menu_price);
			vo.setI_rest(i_rest);
			
			//파일 각각 저장
			MultipartFile mf = fileList.get(i); // 담아놓은 파일리스트를  순서대로 받음
			
			if(mf.isEmpty()) {continue;} //파일이 없으면 스킵
			
			String originFileNm = mf.getOriginalFilename(); // 원본 파일이름을 받아옴
			String ext = FileUtils.getExt(originFileNm); //확장자 받아옴
			String saveFileNm = UUID.randomUUID() + ext; //저장할 파일명을 담음 
			try {
				mf.transferTo(new File(path + saveFileNm)); //경로에 저장할 파일명으로 새로운 파일을 만듬
				vo.setMenu_pic(saveFileNm);
			}catch(Exception e) {
				e.printStackTrace();
			}	
		}
		
		for(RestRecMenuVO vo : list) {
			mapper.insRestRegMenu(vo);
		}
		return i_rest;
	}
	
	
	//ajax 추천메뉴 삭제
	public int delRecMenu(RestPARAM param, String realPath) {
		//실제 파일 삭제
		List<RestRecMenuVO> list = mapper.selRestRecMenus(param); //이미지파일 이름을 가져오기위해 추천메뉴를 셀렉해서옴
		if(list.size() == 1) {
			RestRecMenuVO item =list.get(0); //무조건 배열로 넘어오기때문에 이런식으로 객체를 얻어야함
			
			if(item.getMenu_pic() != null && !item.getMenu_pic().equals("")) {//이미지가 있다면 삭제
				File file = new File(realPath + item.getMenu_pic());
				if(file.exists()) { //파일이 존재하는지(존재한다면 true)
					if(file.delete()) {//file.delete()도 결과값이 boolean으로 성공여부가 날아옴 그걸 이용해서 기록남기면됨
						return mapper.delRestRecMenu(param);
					}else {
						return 0;
						//파일삭제, 가~~끔 삭제가 안될때가있는데 그것도 기록으로 남겨야함(지금은 안했음)
					}
				}
			}
		}
		
		return mapper.delRestRecMenu(param);
	}
}
