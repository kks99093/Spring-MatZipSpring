<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<style>
	.label {margin-bottom: 96px;}
	.label * {display: inline-block;vertical-align: top;}
	.label .left {background: url("https://t1.daumcdn.net/localimg/localimages/07/2011/map/storeview/tip_l.png") no-repeat;display: inline-block;height: 24px;overflow: hidden;vertical-align: top;width: 7px;}
	.label .center {background: url(https://t1.daumcdn.net/localimg/localimages/07/2011/map/storeview/tip_bg.png) repeat-x;display: inline-block;height: 24px;font-size: 12px;line-height: 24px;}
	.label .right {background: url("https://t1.daumcdn.net/localimg/localimages/07/2011/map/storeview/tip_r.png") -1px 0  no-repeat;display: inline-block;height: 24px;overflow: hidden;width: 6px;}
</style>

<div id="sectionContainerCenter">
	<div id="mapContainer" style="width:100%; height:100%;"></div>
	
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=86737adefc889cff0763239e8b04a95a"></script>
	<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
	<script>
		
		var markerList = [] //마커 리스트
		
		const options = { //지도를 생성할 때 필요한 기본 옵션
			center: new kakao.maps.LatLng(35.865552, 128.593393), //지도의 중심좌표.
			level: 5 //지도의 레벨(확대, 축소 정도)
		}
		
		const map = new kakao.maps.Map(mapContainer, options); //지도 생성 및 객체 리턴
								//여기 id값 적음
								
		
		//axios(JSON)을 이용해서 DB에서 데이터를 얻어옴
		function getRestaurantList(){
			//마커 모두 지우기
			markerList.forEach(function(customOverlay){
				customOverlay.setMap(null)
			})
			
			const bounds = map.getBounds(); //현재 지도 영역을 가져옴
			const southWest = bounds.getSouthWest() //남서쪽 끝 좌표 가져옴
			const northEast = bounds.getNorthEast() //북동쪽 끝 좌표 가져옴
			
			console.log('southWest: ' + southWest)
			console.log('northEast: ' + northEast)
			
			const sw_lat = southWest.getLat()
			const sw_lng = southWest.getLng()
			const ne_lat = northEast.getLat()
			const ne_lng = northEast.getLng()
			
			axios.get('/rest/ajaxGetList',{
				params:{
					sw_lat, sw_lng, ne_lat, ne_lng
				}
			}).then(function(res){
				console.log(res.data)
				
				res.data.forEach(function(item){
					createMarker(item)
				})
			})
		}
		kakao.maps.event.addListener(map, 'tilesloaded', getRestaurantList)
	
		//마커를 생성함 (커스텀 오버레이 사용)
		function createMarker(item){
			var content = document.createElement('div')
			content.className = 'label'			
			var leftSpan = document.createElement('span')
			leftSpan.className = 'left'
			var centerSpan = document.createElement('span')
			centerSpan.className = 'center'
			var rightSpan = document.createElement('span')
			rightSpan.className = 'right'	
			
			var restNm = item.nm
			if(item.is_favorite == 1){
				restNm += '★★ '
			}
			centerSpan.innerText = restNm
			
			
			content.appendChild(leftSpan)
			content.appendChild(centerSpan)
			content.appendChild(rightSpan)
			
			//var ctnt = `<div class ="label"><span class="left"></span><span class="center" >\${item.nm}</span><span class="right"></span></div>` //오버레이에 표시할 내용
			var mPos = new kakao.maps.LatLng(item.lat, item.lng) //포지션 정해줌
			
			var customOverlay = new kakao.maps.CustomOverlay({ //오버레이 생성
			    position: mPos,
			    content: content
			})
			
			addEvent(content, 'click', function(){
				console.log('마커클릭 : ' + item.i_rest)
				moveToDetail(item.i_rest)
			})
			customOverlay.setMap(map)
			
			markerList.push(customOverlay)
			
		/*var marker = new kakao.maps.Marker({ //마커생성
				position: mPos
				
			}); 
			
			marker.setMap(map)
		*/
		}
		
		function moveToDetail(i_rest){
			console.log(i_rest)
			location.href='/rest/detail?i_rest='+i_rest
		}
		
		function addEvent(target, type, callback){
			if(target.addEventListener){ //타겟에 addEventListener 있는지 보고 있으면 이거쓰고
				target.addEventListener(type, callback)
			}else{ //addEventListener 없으면 이거씀
				target.attachEvent('on' + type, callback)
			}
		}

		
		
		if (navigator.geolocation){
			console.log('Geolocation is supported!');
			
			var startPos;
			navigator.geolocation.getCurrentPosition(function(position){
				startPos = position
				console.log('lat : ' + startPos.coords.latitude)
				console.log('lng : ' + startPos.coords.longitude)
				if(map){
					var moveLatLon = new kakao.maps.LatLng(startPos.coords.latitude, startPos.coords.longitude)
					map.panTo(moveLatLon)
				}
				
			});
		} else {
			console.log('Geolocation is not supported for this Browser/OS.')
		}
	</script>
</div>