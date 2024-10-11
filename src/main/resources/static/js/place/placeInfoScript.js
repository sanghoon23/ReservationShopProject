// URL에서 파라미터를 가져오는 함수
function getUrlDetailNumber() {
    let regex = /detail\/(\d+)/;
    let match = regex.exec(window.location.href);
    return match ? match[1] : '';
}

function addCommentToDOM(comment) {

    let placeId = getUrlDetailNumber();

    let commentDiv = $('<div class="comment"></div>');

    let headerDiv = $('<div class="comment-header"></div>');

    let userDiv = $('<div class="comment-user"></div>').text(comment.userName);
    let dateDiv = $('<div class="comment-date"></div>').text(comment.createDateTime);
    headerDiv.append(userDiv);
    headerDiv.append(dateDiv);

    let contentDiv = $('<div class="comment-content"></div>').text(comment.content);
    commentDiv.append(headerDiv);
    commentDiv.append(contentDiv);

    if (comment.userId === comment.currentUserId) {
        let editButton = $('<button class="edit-comment">수정</button>').click(function () {
            let newContent = prompt('수정할 내용을 입력하세요:', comment.content);
            if (newContent) {
                $.ajax({
                    url: '/api/place/' + placeId + '/comment/update/' + comment.commentId,
                    method: 'PUT',
                    data: {
                        content: newContent,
                    },
                    success: function (data) {
                        contentDiv.text(newContent);
                    },
                    error: function (error) {
                        alert('댓글 수정 실패');
                        console.error("댓글 수정 실패:", error.responseText);
                    }
                });
            }
        });

        let deleteButton = $('<button class="delete-comment">삭제</button>').click(function () {
            if (confirm('정말 삭제하시겠습니까?')) {
                $.ajax({
                    url: '/api/place/' + placeId + '/comment/delete/' + comment.commentId,
                    method: 'DELETE',
                    success: function () {
                        commentDiv.remove();
                    },
                    error: function (error) {
                        alert('댓글 삭제 실패');
                        console.error("댓글 삭제 실패:", error.responseText);
                    }
                });
            }
        });
    }

    commentDiv.append(editButton);
    commentDiv.append(deleteButton);

    $('#comments').prepend(commentDiv);
}


function loadMap() {
    let placeId = getUrlDetailNumber();

    $.ajax({
        url: '/api/place/map/detail/' + placeId,
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            let address = data.mainAddress + ', ' + data.detailAddress;
            searchAddress(address); // 주소로 지도 검색
        },
        error: function (error) {
            console.error("데이터 로드 실패:", error);
        }
    });
}

function loadComments(){
    let placeId = getUrlDetailNumber();
    $.ajax({
        url: '/api/place/commentList/' + placeId,
        method: 'GET',
        dataType: 'json',
        success: function (data){ // data = commentList 객체
            data.forEach(function(comment) { // comment[i] 객체
                addCommentToDOM(comment);
            });
        },
        error: function (error) {
            console.error("댓글 로드 실패:", error);
        }
    });
}

//comment 로드하기
loadMap();
loadComments();

// 댓글 추가 이벤트 핸들러
$('#commentForm').off('submit').on('submit', function(event) {
    event.preventDefault(); // 기본 폼 제출 방지

    let content = $('#commentContent').val();
    let placeId = getUrlDetailNumber();

    $.ajax({
        url: '/api/place/comment/add/' + placeId,
        method: 'POST',
        headers: {
            'OriginURL': window.location.href // 현재 페이지의 URL을 헤더에 포함
        },
        data: {
            content: content,
        },
        success: function (data) {
            addCommentToDOM(data);
            $('#commentContent').val('');  // 입력 필드 비우기
        },
        error: function (error) {

            if (error.status === 401) {
                console.error("error status: 401", error.responseText);
                window.location.href = '/members/loginMemberPage'; // 로그인 페이지로 리다이렉트
            } else {
                alert('댓글 추가 실패');
                console.error("댓글 추가 실패:", error.responseText);
            }

        }
    });
});
