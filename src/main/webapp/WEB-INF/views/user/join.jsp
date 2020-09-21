<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="join_sectionContainerCenter">
	<div = class="join_div">
		<div class="msg">${msg}</div>
		<form id="frm" class="join_form" action="/user/join" method="post" onsubmit="return chk()">
			<div id="idChkResult" class="msg"></div>
			<div><input type="text" name="user_id" placeholder="아이디">
				<button type="button" onclick="chkId()" name="user_idchk">아이디 중복체크</button>
			</div>
			<div><input type="password" name="user_pw" placeholder="비밀번호"></div>
			<div><input type="password" name="user_pwre" placeholder="비밀번호 확인"></div>
			<div><input type="text" name="nm" placeholder="이름"></div>
			<div><input type="submit" value="회원가입" id="join_submit"></div>
		</form>	
		<div id="join_to_login"><a href="/user/login">로그인</a></div>	
	</div>
	<div class="bg_cover"></div>
</div>

	<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
	<script>
	//ㅡㅡㅡㅡ 아이디 중복 체크 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		function chkId(){
			const user_id = frm.user_id.value
			axios.post('/user/ajaxChk',{
					user_id //post는 params:{}지움, get은 params:{} 적음
			}).then(function(res){
				console.log(res)
				if(res.data == '2'){ //아이디 없음
					idChkResult.innerText = '사용할 수 있는 아이디 입니다'
				}else if(res.data == '3'){ //아이디 중복됨		
					idChkResult.innerText = '이미 사용중입니다'
				}
			})
		}
	//ㅡㅡㅡㅡㅡ아이디 비밀번호 체크 ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		function chk() {
			if(frm.user_id.value.length < 5){
				alert('아이디는 5글자 이상 입력해 주세요')
				return false
			}else if(frm.user_pw.value.length < 5){
				alert('비밀번호는 5글자 이상 입력해주세요')
				return false
			}else if(frm.user_pw.value != frm.user_pwre.value){
				alert('비밀번호를 확인해 주세요')
				return false
			}else if(frm.user_idchk.value == 3){
				alert('아이디 중복확인을 해주세요')
				return false
			}
			return true
				
		}
	</script>
	
</div>