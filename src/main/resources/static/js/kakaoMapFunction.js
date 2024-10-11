function searchAddress(address) {
    // 주소-좌표 변환 객체를 생성합니다
    let geocoder = new kakao.maps.services.Geocoder();

    if(address == null)
    {
        address = document.getElementById('address').value;
    }

    // 주소로 좌표를 검색합니다
    geocoder.addressSearch(address, function (result, status) {

        console.log('주소 검색 결과: ', result, '상태: ', status); // 결과와 상태 확인

        // 정상적으로 검색이 완료됐으면
        if (status === kakao.maps.services.Status.OK) {

            let coords = new kakao.maps.LatLng(result[0].y, result[0].x);

            // 지도를 생성합니다
            let map = new kakao.maps.Map(document.getElementById('map'), {
                center: coords,
                level: 3
            });

            // 결과값으로 받은 위치를 마커로 표시합니다
            let marker = new kakao.maps.Marker({
                map: map,
                position: coords
            });

            // 인포윈도우로 장소에 대한 설명을 표시합니다
            let infowindow = new kakao.maps.InfoWindow({
                content: '<div style="width:150px;text-align:center;padding:6px 0;">' + result[0].address_name + '</div>'
            });
            infowindow.open(map, marker);

            // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
            map.setCenter(coords);
        } else {
            alert('주소 검색에 실패했습니다.');
        }
    });

}
//
// document.addEventListener('DOMContentLoaded', (event) => {
//     searchAddress();
// });