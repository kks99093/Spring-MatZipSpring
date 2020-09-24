package com.koreait.matzip.rest;


import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.koreait.matzip.Const;
import com.koreait.matzip.SecurityUtils;
import com.koreait.matzip.ViewRef;
import com.koreait.matzip.model.RestFile;
import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestPARAM;

@Controller //Controller박으면 handdlerMapper가 주시하는애라는 뜻
@RequestMapping("/rest")
public class RestController {
	
	@Autowired
	private RestService service;
	
	@RequestMapping("/map") //한개만 적을거면 이렇게만해도됨, Method는 기본이 GET이라 이렇게만 적어놓음
	public String restMap(Model model) {
		model.addAttribute(Const.TITLE,"지도 보기");
		model.addAttribute(Const.VIEW,"rest/restMap");
		return ViewRef.TEMP_MENU;
	}
	
	//화면에 보이는영역에 있는 restaurant 정보 가져오기
	@RequestMapping(value = "/ajaxGetList", produces = "application/json; charset=utf8")//ResponsBody 인코딩설정을 해줘야함
	@ResponseBody  public List<RestDMI> ajaxGetList(RestPARAM param) {
	//이렇게 @ResponseBody 뒤에 바로적어도됨
		return service.selRestList(param);
		//그냥 객체를 보내면 스프링이 알아서 JSON형태로 바꿔서 리턴을 해준다
		//4.대 스프링은 jackson을 기본 라이브러리로 다운받아 주기때문에 라이브러리도 따로 받을필요 없다
	}
	
	//restaurant 등록
	
	@RequestMapping(value="/reg", method = RequestMethod.GET)
	public String restReg(Model model) {
		model.addAttribute("categoryList",service.selCategoryList());
		model.addAttribute(Const.TITLE,"가게 등록");
		model.addAttribute(Const.VIEW,"rest/restReg");
		return ViewRef.TEMP_MENU;
	}
	
	@RequestMapping(value="/reg", method = RequestMethod.POST)
	public String insReg(RestPARAM param, HttpSession hs) {
		param.setI_user(SecurityUtils.getLoginUserPk(hs));
		int result = service.insRest(param);
		if(result != 1) { //insert 실패시
			return "redirect:/rest/reg";
		}
		return "redirect:/";
	}
	
	//restaurant 디테일 가기
	@RequestMapping("/detail")
	public String detail(Model model,RestPARAM param){
		int hitsresult = service.updHits(param);//조회수
		
		RestDMI data = service.selRest(param);
		model.addAttribute("css",new String[] {"restaurant"});
		model.addAttribute("recMenuList", service.selResMenus(param));
		model.addAttribute("menuList",service.selMenus(param));
		model.addAttribute("data", data);
		model.addAttribute(Const.TITLE,data.getNm());//가게명
		model.addAttribute(Const.VIEW,"rest/restDetail");
	return ViewRef.TEMP_MENU;
	}
	//restaurant 삭제
	@RequestMapping("/del")
	public String delRest(Model model, RestPARAM param, HttpSession hs){
		param.setI_user(SecurityUtils.getLoginUserPk(hs));
		int result = 1;
		//@Transactional은 이미 try-cahtch를 쓰고있기떄문에(선생님예상) 서비스에서가 아닌 여기서 try-cahtch를 걸어준다
		//try-catch를 안하면 에러가터졌을때 우리가 적은 쿼리문이 찍힌다(해킹당하기 좋은상태), 그렇기떄문에 try-catch해줌
		try {
			service.delRestTran(param);
		} catch (Exception e) {
			result = 0;
		}
		
		return "redirect:/";
	}
	
	//추천메뉴 등록(파일 업로드)
	@RequestMapping(value="/recMenus", method=RequestMethod.POST)
	//메소드가 한개라면 POST,GET안적어줘도 알아서 넘어오게 해주는듯?, 메소드가 2개라면 method 해줘야하는듯?
	public String recMenus(MultipartHttpServletRequest mReq, RedirectAttributes rs) {		
		
		int i_rest = service.insResMenus(mReq);
		
		//addAttribute는 쿼리스트링 만드는것, addFlashAttribute는 쿼리스트링을 안만들고 세션을 사용해서 값을전달하고 사용후 바로 삭제
		rs.addAttribute("i_rest", i_rest);
		return "redirect:/rest/detail";
	}
	//추천메뉴 삭제
	@RequestMapping(value="/ajaxDelRecMenu", method=RequestMethod.GET)
	@ResponseBody public int ajaxDelRecMenu(RestPARAM param, HttpSession hs) {
		
		
		param.setI_user(SecurityUtils.getLoginUserPk(hs)); //로긴 유저 pk 담기
		return service.delRecMenu(param, hs);
	}
	
	//메뉴 등록(multiple 파일 업로드)
	@RequestMapping("/menus")
	public String menus( RestFile param 
			//ModelAttribute는 파라미터를 객체로 받을때  써줘야함, restPARAM도 원래는 써줘야함 그런데 Spring이 알아서 해주는듯?
			//안적어줘도됨	 
			, HttpSession hs
			, RedirectAttributes ra) {
						//다중 객체를 받을때 사용하는거(model 패키지에 RestFile 클래스 만들어놓음)
//		for(MultipartFile file : param.getMenu_pic()) {
//			System.out.println("fileNm : " + file.getOriginalFilename());
//		}
		int i_user = SecurityUtils.getLoginUserPk(hs);
		int result = service.insMenus(param, i_user);
		return"redirect:/rest/detail?i_rest=" + param.getI_rest();
	}
	
}
