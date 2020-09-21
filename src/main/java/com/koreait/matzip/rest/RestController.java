package com.koreait.matzip.rest;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.koreait.matzip.Const;
import com.koreait.matzip.ViewRef;
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
	@RequestMapping("/ajaxGetList")
	@ResponseBody  public String ajaxGetList(RestPARAM param) {
	//이렇게 @ResponseBody 뒤에 바로적어도됨
		
		return service.selRestList(param);
		
	}
	//restaurant 등록
	
	@RequestMapping(value="/reg", method = RequestMethod.GET)
	public String restReg(Model model) {
		model.addAttribute(Const.TITLE,"가게 등록");
		model.addAttribute(Const.VIEW,"rest/restReg");
		return ViewRef.TEMP_MENU;
	}
	
	@RequestMapping(value="/reg", method = RequestMethod.POST)
	public String insReg(RestPARAM param, HttpSession hs) {
		UserVO vo = (UserVO)hs.getAttribute(Const.LOGIN_USER);
		param.setI_user(vo.getI_user());
		service.insReg(param);
		return"redirect:/rest/map";
	}
	
	
}
