<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--template정보를 가져올거기때문에 html이 있으면 안된다 --%>
<div id="login_conbg"></div>
<div id="login_sectionContainerCenter">
	<div id="login_border">		
			<form id="frm" class="login_form" action="/user/login" method="post">
				<div><input type="text" name="user_id" placeholder="아이디"></div>
				<div><input type="password" name="user_pw" placeholder="비밀번호"></div>
				<div class="msg">${msg}</div>
				<div><input type="submit" id="login_submit" value=""></div>
				<a href="/user/join">회원가입</a>
			</form>			
		<div id="login_bg"></div>
	</div>
</div>
	