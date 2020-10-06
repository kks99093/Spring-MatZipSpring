package com.koreait.matzip.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.koreait.matzip.CommonUtils;
import com.koreait.matzip.Const;
import com.koreait.matzip.FileUtils;
import com.koreait.matzip.SecurityUtils;
import com.koreait.matzip.model.CodeVO;
import com.koreait.matzip.model.CommonMapper;
import com.koreait.matzip.model.RestFile;
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
	
	//조회수 올리기
	public void addHits(RestPARAM param, HttpServletRequest req) {	
		//마지막으로 detail에 들어갔던 사람의 ip값을 얻어와서 나의 ip와 비교해서 조회수를 올리지 말지 결정
		int i_user = SecurityUtils.getLoginUserPk(req);
		
		String myIp = req.getRemoteAddr(); //현재 들어온 사람의 ip주소를 얻어옴
		ServletContext ctx = req.getServletContext(); //어플리케이션을 불러옴
		//request,session,pageContext는 개인용,		어플리케이션은 공용(서버마다 1개밖에 안만들어짐)
		
		String currentRestReadIp = (String)ctx.getAttribute(Const.CURRENT_REST_RESAD_IP + param.getI_rest()); 
		//마지막으로  detail에 들어갔던사람(어플리케이션을 이용해 얻어옴,Const.CURRENT_REST_RESAD_IP + param.getI_rest()라는 키값에 저장되어있음 )
		//(ex. 5번 글을 읽는다면 Const.CURRENT_REST_RESAD_IP + 5의 키값을 가져옴)
		
		//마지막 들어갔던사람이 없거나 (null 내가 첫입장)/ 마지막에 들어갔던사람과 현재들어가는 사람이 다를경우 (ip를 비교)
		if(currentRestReadIp == null || !currentRestReadIp.equals(myIp)) {
			//조회수 올림 처리 할거
			param.setI_user(i_user);
			//내가 쓴글이면 조회수로 안올라가게 쿼리문으로 막을거
			
			mapper.updAddHits(param);
		
			//어플리케이션에 마지막에 들어온사람의 ip를  키값에다가 넣어놓음
			ctx.setAttribute(Const.CURRENT_REST_RESAD_IP + param.getI_rest(), myIp);
						//currentRestReadIp_같은 경우는 실수할수도 있으니 Const로 빼놓음
		}
	}
	
	// 디테일 셀렉트
	public RestDMI selRest(RestPARAM param) {
		return mapper.selRest(param);
	}
	// 추천메뉴 셀렉트
	public List<RestRecMenuVO> selRestRecMenus(RestPARAM param) {
		return mapper.selRestRecMenus(param);
	}
	//메뉴 셀렉트
	public List<RestRecMenuVO> selMenus(RestPARAM param){
		return mapper.selRestMenus(param);
	}
	
	//트랜잭션으로 레스토랑 삭제
	@Transactional
	public void delRestTran(RestPARAM param) {
		mapper.delRestRecMenu(param);
		mapper.delRestMenu(param);
		mapper.delRest(param);
		//밑의 메소드를 호출해도됨
	}
	
	//추천메뉴 등록(업로드)
	public int insResMenus(MultipartHttpServletRequest mReq) {
		int i_user = SecurityUtils.getLoginUserPk(mReq.getSession());
		int i_rest = Integer.parseInt(mReq.getParameter("i_rest"));
		
		if(_authFail(i_rest,i_user)) { //인증실패 유무, true라면 인증실패
			return Const.FAIL; //인증 실패했다면 바로 0을 리턴
		}
		
		
		List<MultipartFile> fileList = mReq.getFiles("menu_pic"); //이미지파일을 담는 List(배열생성) 
		String[] menuNmArr = mReq.getParameterValues("menu_nm"); //메뉴이름을 담는 String배열
		String[] menuPriceArr = mReq.getParameterValues("menu_price"); //가격을 담는 String 배열
		
		String path = Const.realPath + "/resources/img/rest/" + i_rest + "/rec_menu/";
		//저장위치 얻어오기(드라이브~프로젝트작업공간까지의 절대주소), pome.xml에서 서블릿,jsp를 최신버전으로 바꿔줘야함
		System.out.println(path);
		FileUtils.makeFolder(path);
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
	public int delRestRecMenu(RestPARAM param,  HttpSession hs) {
		//실제 파일 삭제
		//여기서는 파일명을 셀렉트로 받아옴
		List<RestRecMenuVO> list = mapper.selRestRecMenus(param); //이미지파일 이름을 가져오기위해 추천메뉴를 셀렉해서옴
		String path = Const.realPath + "/resources/img/rest/" + param.getI_rest() + "/rec_menu/";
		
		if(list.size() == 1) {
			RestRecMenuVO item =list.get(0); //무조건 배열로 넘어오기때문에 이런식으로 객체를 얻어야함
			
			if(item.getMenu_pic() != null && !"".equals(item.getMenu_pic())) {//이미지가 있다면 삭제
				//왠만하면 "".equals를 써야함 null.equals가 되어버리면 null은 객체가 아니기때문에 에러가 터짐(""은 이미 저자체로 객체가 있는것)
				File file = new File(path + item.getMenu_pic());
				System.out.println(path + item.getMenu_pic());
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
	
	// ajax 메뉴 삭제
	public int delRestMenu(RestPARAM param) {	
		//파일명까지 같이 param에 담아줘서 보냄
		if(param.getMenu_pic() != null && !"".equals(param.getMenu_pic())){
			String path = Const.realPath + "/resources/img/rest/" + param.getI_rest() + "/menu/";
			
			if(FileUtils.delFile(path + param.getMenu_pic())) {
				return mapper.delRestMenu(param);
			}else {
				return Const.FAIL;
			}
		}
		return mapper.delRestMenu(param);
	}

	
	//메뉴 등록
	public int insMenus(RestFile param, int i_user) {
		
		if(_authFail(param.getI_rest(),i_user)) { //인증실패 유무, true라면 인증실패
			return Const.FAIL; //인증 실패했다면 바로 0을 리턴
		}
		
		String path = Const.realPath + "/resources/img/rest/" + param.getI_rest() + "/menu/";
		FileUtils.makeFolder(path);
		List<RestRecMenuVO> list = new ArrayList();
		
		for(MultipartFile mf : param.getMenu_pic()) {
			RestRecMenuVO vo = new RestRecMenuVO();
			list.add(vo);
			
			//null이 박혀있으면 파일이 없다는것, 문자열이 박혀있다면 파일이 있다는것
			String saveFileNm = FileUtils.saveFile(path, mf); //UUID로 파일명을 만들면서 디렉터리에 파일생성까지 해주는 메소드를 만듬
			vo.setMenu_pic(saveFileNm);
			vo.setI_rest(param.getI_rest());
		
		}
		
		for(RestRecMenuVO vo : list) {
			mapper.insMenus(vo);
		}
		return Const.SUCCESS;
	}
	
	//글쓴이인증 셀렉트 - 장난질 막음, i_user를 같이 보내서 sel했는데 검색이 안된다면 장난질이라는거
	private boolean _authFail(int i_rest, int i_user) { //private는 _로 시작
		RestPARAM param = new RestPARAM();
		param.setI_rest(i_rest);
		
		RestDMI dbResult = mapper.selRest(param);
		if(dbResult == null || dbResult.getI_user() != i_user) {
			return true;
		}
		return false;
	}
}
