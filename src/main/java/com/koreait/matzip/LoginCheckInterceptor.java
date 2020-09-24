package com.koreait.matzip;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

//로그인 유무체크하는 인터셉터
public class LoginCheckInterceptor extends HandlerInterceptorAdapter{
										//인터셉터를 하려면 얘를 상속 받아야함
	/* 
	ㅡㅡㅡLoginCheckInterceptor 매소드종류 4가지ㅡㅡㅡ 
	preHandle  - Controller실행하기전 실행 
	postHandle - Controller 끝나면 실행
	afterComplete - view가 정상적으로 실행된 후에 실행
	afterConcurrentHandlingStrated - 비동기 요청시
	필요한 매개변수랑 사용법은 검색해서 쓰면됨
	*/
	
	//로그인 유무체크 (Controller에서 Mapping 되어있는 애들만 인터셉터에걸림)
	@Override       
	public boolean preHandle(HttpServletRequest request,
		HttpServletResponse response, Object handler) throws Exception{
		String uri = request.getRequestURI(); //주소값을 받아옴
		String[] uriArr = uri.split("/");

		
		if(uri.equals("/")) { // /(인덱스)로 들어올경우는 바로 통과시킴
			return true;
		}else if(uriArr[1].equals("res")) { //css,js,img같은것들은 그냥 통과시킴
			return true;
		} 
		
		boolean isLogout = SecurityUtils.isLogout(request);
		
		//여기서 로그인없이 쓸수있는지 없는지를 나눠줄거
		//기본이 true고 false만 골라낼꺼
		switch(uriArr[1]) { 
		case ViewRef.URI_USER: //user일때 체크
			switch(uriArr[2]) {
			case "login": case "join":
				if(!isLogout) { //로그인 되어있는 상태
					response.sendRedirect("/rest/map");
					return false;
				}
			}
			
		case ViewRef.URI_REST: //rest일때 체크
			switch(uriArr[2]) { 
			case "reg":
				if(isLogout) { //로그인 안되어있는 상태
					response.sendRedirect("/user/login");
					return false;
				}
				
			}
			
		}
		
		return true;
	}
}
