<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div class="recMenuContainer">
	<c:forEach items="${recMenuList}" var="item">
		<div class="recMenuItem" id="recMenuItem_${item.seq}">
			<div class="pic">
				<c:if test="${item.menu_pic != null && item.menu_pic !='' }">
					<img src="/res/img/rest/${data.i_rest}/rec_menu/${item.menu_pic}">
				</c:if>
			</div>
			<div class="info">
				<div class="nm">${item.menu_nm}</div>
				<div class="price"><fmt:formatNumber type="number" value="${item.menu_price}"/></div>						
			</div>
			<c:if test="${loginUser.i_user == data.i_user}">
				<div class="delIconContainer" onclick="delRecMenu(${item.seq})">
					<span class="material-icons">clear</span>
				</div>							
			</c:if>
		</div>
	</c:forEach>
</div>
<div id="sectionContainerCenter">
	<div>
		<c:if test="${loginUser.i_user == data.i_user}">
			<div><button onclick="isDel()">가게 삭제</button></div>			
			<h2>- 추천 메뉴 -</h2>
			<div>
				<div><button type="button" onclick="addRecMenu()">추천 메뉴 추가</button></div>
				<form id="recFrm" action="/rest/recMenus" enctype="multipart/form-data" method="post">					
					<input type="hidden" name="i_rest" value="${data.i_rest}">
					<div id="recItem">
					</div>
					<div><input type="submit" value="등록"></div>
				</form>
			</div>
			<h2>- 메뉴 -</h2>
			<div>
				<form id=menuFrm" action="/rest/menus" enctype="multipart/form-data" method="post">
					<input type="hidden" name="i_rest" value="${data.i_rest}">
					<input type="file" name="menu_pic" multiple>
					<div id="menuItem"></div>
					<div><input type="submit" value="등록"></div>
				</form>
			</div>
		</c:if>
		<div class="restaurant-detail">
			<div id="detail-header">
				<div class=restaurant_title_wrap">
					<span class="title">
						<h1 class="restaurant_name">${data.nm}</h1>						
					</span>
				</div>
				<div class="status branch_none">
					<span class="cnt hit">${data.hits}</span>
					<span class="cnt favorite">${data.cnt_favorite}</span>
				</div>
			</div>
				<table>
					<caption>레스토랑 상세 정보</caption>
					<tbody>
						<tr>
							<th>주소</th>
							<td>${data.addr}</td>
						</tr>
						<tr>
							<th>카테고리</th>
							<td>${data.cd_category_nm}</td>
						</tr>
						<tr>
							<th>작성자</th>
							<td>${data.nm }</td>
						</tr>
						<tr>
							<th>메뉴</th>
								<td>	
									<div id="conMenuList"  class="menuList" >
									</div>
								</td>
						</tr>
					</tbody>					
				</table>
		</div>
	</div>
</div>
<div id="carouselContainer" class="padeShow">
<!-- 이런걸 모바일에서는 modal, 웹에서는 hover라고 함 -->
	<div id="imgContainer">
		<div class="swiper-container">
			<div id="swiperWrapper" class="swiper-wrapper">
	        <!-- Slides -->
		    </div>
		    <!-- If we need pagination -->
		    <div class="swiper-pagination"></div>
		
		    <!-- If we need navigation buttons -->
		    <div class="swiper-button-prev"></div>
		    <div class="swiper-button-next"></div>
		</div>
	</div>
	<div>
		<span class="material-icons" onclick="closeCarousel()">clear</span>
	</div>	

</div>

<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script src="https://unpkg.com/swiper/swiper-bundle.min.js"></script>
<script>

//스와이퍼
		//닫기
	function closeCarousel(){
		carouselContainer.style.opacity = 0
		carouselContainer.style.zIndex = -10
	}
	
		//열기
	function openCarousle(idx){
		mySwiper.slideTo(idx)
		carouselContainer.style.opacity = 1
		carouselContainer.style.zIndex = 40
		
	}
		//스와이퍼
	var mySwiper = new Swiper('.swiper-container', {
		  // Optional parameters
		  direction: 'horizontal',
		  loop: true,
		
		  // If we need pagination
		  pagination: {
		    el: '.swiper-pagination',
		  },
		  // Navigation arrows
		  navigation: {
		    nextEl: '.swiper-button-next',
		    prevEl: '.swiper-button-prev',
		  }
	
		})
	
//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//메뉴 셀렉트 Ajax ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	var menuList = []
	function ajaxSelMenuList(){
		axios.get('/rest/ajaxSelMenuList', {
			params: {
				i_rest: ${data.i_rest}
			}
		}).then(function(res){ //응답의 모든정보가 res에 담김
			menuList = res.data//res는 정보, .data에 모든데이터가 담겨있음
			refreshMenu()
		})
	}

	function refreshMenu(){
		conMenuList.innerHTML = '' //일단 비워놓음
		swiperWrapper.innerHTML = ''
		menuList.forEach(function(item, idx){ //forEach가 원래 idx를 그냥 보내는데 그걸 파라미터에 idx를 만들어서 받아줘야함
			makeMenuItem(item, idx) //for문을 돌면서 makeMenuItem에게 아이템을 1개씩 보냄, 그러면 makeMenuItem이 메뉴를 하나씩 만들거
		})
		
	}
	
	function makeMenuItem(item, idx){
		//메뉴용 이미지
		const div = document.createElement('div')
		div.setAttribute('class', 'menuItem') //css를위해 클래스명을 넣어놓음
		
		
		const img = document.createElement('img')
		img.setAttribute('src', `/res/img/rest/${data.i_rest}/menu/\${item.menu_pic}`)
		img.style.cursor = 'pointer'
		img.addEventListener('click', function(){
			openCarousle(idx+1)
		})
		div.append(img)
		
		//스와이프용 이미지
		const swiperDiv = document.createElement('div')
		swiperDiv.setAttribute('class', 'swiper-slide')
		
		
		const swiperImg = document.createElement('img')
		swiperImg.setAttribute('src', `/res/img/rest/${data.i_rest}/menu/\${item.menu_pic}`)
		
		swiperDiv.append(swiperImg)
		mySwiper.appendSlide(swiperDiv);
		
		
		
		if(${loginUser.i_user == data.i_user}){ //여기 jstl 쓸수있음
			const delDiv = document.createElement('div')
			delDiv.setAttribute('class','delIconContainer')
			delDiv.addEventListener('click', function(){
				if(idx > -1){
					//서버에 삭제 요청!
					axios.get('/rest/ajaxDelMenu',{
						params:{
							i_rest: ${data.i_rest},
							seq : item.seq,
							menu_pic : item.menu_pic
						}
					}).then(function(res){
						if(res.data == 1){
							menuList.splice(idx, 1)
							refreshMenu()	
						} else {
							alert('메뉴를 삭제할 수 없습니다')
						}
					})
					
					
				}
			})
			
			const span = document.createElement('span')
			span.setAttribute('class', 'material-icons')
			span.innerText = 'clear'
			
			delDiv.append(span)
			div.append(delDiv)
		}
			
		conMenuList.append(div)
		swiperWrapper.append(swiperDiv)
	}
	
	ajaxSelMenuList()

//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	function delRecMenu(seq){
		
		axios.get('/rest/ajaxDelRecMenu',{
			params: {
				i_rest: ${data.i_rest},
				seq: seq
			}
		}).then(function(res){
			if(res.data == 1){
				//엘리먼트 삭제
				var ele = document.querySelector('#recMenuItem_' + seq)
				ele.remove()
			}
		})
	}
	
	
	//추천메뉴 등록인풋 추가, 로그인 안됐다면 호출이 안되게해놓음
	if(${loginUser.i_user == data.i_user}){
	var idx = 0;
	function addRecMenu(){
		var div = document.createElement('div')
		div.setAttribute('id', 'recMenu_' + idx++)
		
		var inputNm = document.createElement('input') //menu_nm 인풋 만드는부분
		inputNm.setAttribute('type','text')
		inputNm.setAttribute('name', 'menu_nm')
		var inputPrice = document.createElement('input') //menu_price 인풋 만드는 부분
		inputPrice.setAttribute('type','number')
		inputPrice.setAttribute('name', 'menu_price')
		inputPrice.value= '0'
		var inputPic = document.createElement('input') //menu_pic 인풋 만드는 부분
		inputPic.setAttribute('type','file')
		inputPic.setAttribute('name', 'menu_pic')
		var delBtn = document.createElement('input') //삭제버튼 인풋 만드는 부분
		delBtn.setAttribute('type', 'button')
		delBtn.setAttribute('value', 'X')
				
		delBtn.addEventListener('click', function(){
			div.remove()
		})
		
		div.append('메뉴 : ') //문자열은 append만 됨
		div.append(inputNm) //이때는 appendchild도 가능
		div.append(' 가격 : ')
		div.append(inputPrice)
		div.append(' 사진 : ')
		div.append(inputPic)
		div.append(delBtn)
		
		recItem.append(div)
	}
	addRecMenu()
	}
	 function isDel(){
		 if(confirm('삭제 하시겠습니까?')){
			 location.href = '/rest/del?i_rest=${data.i_rest}'
		 }
	 }
	 
</script>	