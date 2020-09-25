package com.koreait.matzip;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.koreait.matzip.rest.RestMapper;

public class RestInterceptor extends HandlerInterceptorAdapter {
									//인터셉터할때 필수
//이렇게 rest에다가도 인터셉터를 걸어놓으면 뒤에서 굳이 장난질 체크를 할필요 없다(소스가 더 깔끔해짐)
	@Autowired
	private RestMapper mapper;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception{
		System.out.println("rest - interceptor");
		
		String uri = request.getRequestURI(); //주소값을 받아옴
		String[] uriArr = uri.split("/");
		System.out.println("uriArr[2] : " + uriArr[2]);
		String[] checkKeywords = {"del", "Del", "upd", "Upd"};
		//인터셉터할 키워드를 배열에 넣어놓음
		for(String keyword: checkKeywords) {
			if(uriArr[2].contains(keyword)) {
				// A.contains(keyword) - A에 keyword의 문장이 있는지 체크 
				int i_rest = CommonUtils.getIntParameter(request, "i_rest");
				if(i_rest == 0) { //i_rest가 0이면 디테일에 안들어온거
					return false;
				}
				int i_user = SecurityUtils.getLoginUserPk(request); //로그인한사람의 i_user
				boolean result = _authSuccess(i_rest, i_user); 
				//글쓴사람과 로그인한사람이 같은지 체크함(t_restaurant에 저장되어있는 i_user와 로그인한사람의 i_user를 비교하는것)
				System.out.println("=== auth result : " + result);
				return result;
			}
		}
		
		return true;
	}
	
	private boolean _authSuccess(int i_rest, int i_user) { //private는 _로 시작
		return i_user == mapper.selRestChkUser(i_rest);
		//i_rest로 t_restaurant를 검색해서 나온 i_user와 로그인한사람의 i_user가 같은지 체크하는 부분
		//(글쓴사람과 로그인사람이 같은지 확인)
		
	}

}
