package com.koreait.matzip;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexContoroller {

	@RequestMapping("/")//얘는 서버 켜지자마자 바로 실행됨 = realPath가 바로 저장됨
	public String index(HttpServletRequest req) {
		if(Const.realPath == null) {
			Const.realPath = req.getServletContext().getRealPath("");//절대주소 저장
		}
		System.out.println("root!!");
		return "redirect:/rest/map";
	}
}
