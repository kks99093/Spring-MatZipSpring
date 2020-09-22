package com.koreait.matzip.rest;


import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.koreait.matzip.Const;
import com.koreait.matzip.SecurityUtils;
import com.koreait.matzip.ViewRef;
import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestPARAM;
import com.koreait.matzip.user.model.UserVO;

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
		return "redirect:/rest/map";
	}
	
	//restaurant 디테일 가기
	@RequestMapping("/detail")
	public String detail(Model model,RestPARAM param){
		
		RestDMI data = service.selRest(param);
		model.addAttribute("data", data);
		model.addAttribute(Const.TITLE,data.getNm());//가게명
		model.addAttribute(Const.VIEW,"rest/restDetail");
	return ViewRef.TEMP_MENU;
	}
	
}
