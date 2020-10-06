package com.koreait.matzip.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.koreait.matzip.Const;
import com.koreait.matzip.SecurityUtils;
import com.koreait.matzip.ViewRef;
import com.koreait.matzip.user.model.UserPARAM;
import com.koreait.matzip.user.model.UserVO;

@Controller
@RequestMapping("/user")
public class UserController {
	/*return
	 1.그냥 String만 붙인다면 jsp파일을 찾아서 연다
	 2.redirect:를 붙인다면 맵핑되는 메소드를 찾아감
	 3.@ResponseBody를 붙이면 jsp파일을 찾는게 아닌 값자체를 리턴함
	 -Controller 앞에 rest를 붙여서 restController라고 하면 모든 메소드에 @ResponseBody가 적용됨
	*/
	@Autowired //bean등록 된애들중에서 service에 넣을수있는애가 있으면 자동으로 넣어준다
	private UserService service;
	//bean등록 - 클래스위에 @적힌애,SpringContainer가 관리하는애
	//Autowired는 여기에 넣을수있는애가 1개만있어야함 여러개라면 에러터짐(어떤걸 넣어야할지 모르니)
	
	

//로그인ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String login(Model model) { //화면(jsp파일)한테 뭔가 보내줘야할때는 model을 적어줘야함

		model.addAttribute(Const.TITLE, "로그인");
		model.addAttribute(Const.VIEW, "user/login");
		return ViewRef.TEMP_DEFAULT;
		//여기 String만 적으면 이 jsp파일을 찾음
	}

	@RequestMapping(value="/login", method = RequestMethod.POST)
	//같은주소값의 GET으로 날렸을떄 POST로 날렸을때 바꿀수있음
	public String login(UserPARAM param, HttpSession hs, RedirectAttributes rs) { //여기서 중요한거 우리가 getParameter를 하지 않았다는거
									//스프링은 이렇게 적으면 그냥 세션을 바로 줌
		// POST에서 GET으로 쿼리스트링을 보내는게 아니라 값(객체)을 바로 Attribute해서 보내는것
		// RedirectAttributes쓰면 회원가입때처럼 쿼리스트링 안쓰고 가능, @RequestParam(defaultValue="0") int err 둘중 택1해서 쓰면됨
		// RedirectAttributes는 일처리를 먼저하는것 , @RequestParam(defaultValue="0") int err는 일처리를 한단계뒤로 미루는것
		int result = service.login(param);
		
		if(result == Const.SUCCESS) {
			System.out.println("로그인 성공");
			hs.setAttribute(Const.LOGIN_USER, param);
			return "redirect:/";
		}
		String msg = null;
		if(result == Const.NO_ID) {
			msg = "아이디를 확인해 주세요. ";
		} else if(result == Const.NO_PW) {
			msg = "비밀번호를 확인해 주세요. ";
		}
		param.setMsg(msg);
		rs.addFlashAttribute("data", param);
		//이값은 세션에 박히고 사용한후에 자동으로 세션에서 삭제됨, 그래서 마치 POST처럼 쓸수있는것
		//addAttribute= 쿼리스트링으로 만들어줌, addFlashAttribute=세션에 값을 잠시박았다가 사용후 지움
		return "redirect:/user/login";
	}
	
//로그아웃 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(HttpSession hs) {
		hs.invalidate();
		return "redirect:/";
	}
	
//회원가입ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	@RequestMapping(value="/join", method = RequestMethod.GET)
	public String join(Model model, @RequestParam(defaultValue="0") int err) {
		//@RequestParam ==> 한값만 받을떄 쓰는것, 원래는 무조건 String이지만 바로 Integer를 적을수있다(null이 들어갈수도있기에 int가 아닌 Integer를 적음)
		//쿼리스트링에 보내는 키값과 저기에 적은벼수(err)이 같다면 @RequestParam(required=false) int err이렇게만적어도됨
		//다르다면 @RequestParam(value="err", required=false) int error라고 적어줘야함
		// required =>필수인지 묻는것, 첫페이지에는 없다가 에러떳을때만 생김으로 false를 줘야함
		// 기본값이 true로 되어있기때문에 err같은경우는 false를 해줘야함
		//Integer를 쓰기 싫다면 defaultValue="0"로 기본값을 0으로 줘놓으면됨 그러면 required=false도 필요없음
		System.out.println("err : " + err);
		if(err > 0) {
			model.addAttribute("msg", "에러가 발생하였습니다.");
		}
		model.addAttribute(Const.TITLE, "회원가입");
		model.addAttribute(Const.VIEW, "user/join");
		return ViewRef.TEMP_DEFAULT;
	}
	
	@RequestMapping(value="/join", method = RequestMethod.POST)
	public String join(UserVO param, RedirectAttributes rs) {
					//여기에 setter가 있는 객체를 적어놓는다면, 이름이 같다면 정보가 다 담겨있다
					//ex)회원가입에서 user_id,user_pw,nm를 넘겨주는데 
					//UserVO에 user_id,user_pw,nm가 있다면 알아서 param에 넣어서 넘겨준다
		int result = service.join(param);
		
		if(result == 1) {
			return "redirect:/user/login";
			//redirect를 붙이면 주소랑 맵핑되는애를 찾음
		}
		rs.addAttribute("err", result);
		//쿼리스트링에 값을 박아줌  ("키값",벨류값)
		//addAttribute= 쿼리스트링으로 만들어줌, addFlashAttribute=세션에 값을 잠시박았다가 사용후 지움
		return "redirect:/user/join";
	}
	
	//아이디 중복체크
	@RequestMapping(value="/ajaxChk", method = RequestMethod.POST)
	@ResponseBody //얘를 붙이면 jsp파일을 찾는것이아닌 그값자체를 넘김
	//만약 얘를 안적었다면 retrun되는 문자열의 jsp파일을 찾았을것
	public String ajaxIdChk(@RequestBody UserPARAM param) {
		int result = service.login(param);
		//위의 로그인을 활용해서 아이디 체크를 할거
		return String.valueOf(result);//정수값 2가 넘어오기때문에 String으로 형변환 해줌
	}
	
	@RequestMapping(value="/ajaxToggleFavorite", method = RequestMethod.GET)
	@ResponseBody
	public int ajaxToggleFavorite(UserPARAM param, HttpSession hs) {
						//Ajax Get받식으로 받을떄 @RequestBody 적으면 안됨
		int i_user = SecurityUtils.getLoginUserPk(hs);
		param.setI_user(i_user);
		return service.ajaxToggleFavorite(param);
	}
	
	@RequestMapping("/favorite")
	public String favorite(Model model,HttpSession hs){
		int i_user = SecurityUtils.getLoginUserPk(hs);
		
		UserPARAM param = new UserPARAM();
		param.setI_user(i_user);
		
		model.addAttribute("data", service.selFavoriteList(param)); //로그인한 사람의 찜리스트
		
		model.addAttribute(Const.CSS, new String[]{"userFavorite", "common"});
		model.addAttribute(Const.TITLE, "찜 리스트");
		model.addAttribute(Const.VIEW, "user/favorite");
		
		return ViewRef.TEMP_MENU;
	}
}
