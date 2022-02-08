/**
	2. 스토리 페이지
	(1) 스토리 로드하기
	(2) 스토리 스크롤 페이징하기
	(3) 좋아요, 안좋아요
	(4) 댓글쓰기
	(5) 댓글삭제
 */
const principalId = $("#principalId").val();
let search = $("#story-search").val();
let page = 0;
storyLoad();
// (1) 스토리 로드하기
function storyLoad(hashtag = "") {
    if(hashtag.startsWith("#")){
         search = hashtag.substring(1);
    }

    $.ajax({
        url:`/api/image?search=${search}&page=${page}`,
        dataType:"json"
    }).done(res=>{
        res.data.content.forEach((image)=>{
            const storyItem = getStoryItem(image);
            $("#storyList").append(storyItem);
        });
    }).fail(error=>{
        console.log("이미지 불러오기 실패",error);
    });
}

function getStoryItem(image) {
    let item = `
    <div class="story-list__item">
        <div class="sl__item__header">
            <a href="/user/${image.user.id}">
                <div>
                    <img class="profile-image" src="/upload/${image.user.profileImageUrl}"
                        onerror="this.src='/images/logo.jpg'" />
                </div>
                <div>${image.user.username}</div>
            </a>
        </div>
        <div class="sl__item__img">
            <img src="/upload/${image.postImageUrl}" />
        </div>
        <div class="sl__item__contents">
            <div class="sl__item__contents__icon">
                <button>`;
                if(image.likeState){
                   item += `<i class="fas fa-heart active" id="storyLikeIcon-${image.id}" onclick="toggleLike(${image.id})"></i>`;
                } else {
                   item += `<i class="far fa-heart" id="storyLikeIcon-${image.id}" onclick="toggleLike(${image.id})"></i>`;
                }
                item += `
                </button>
            </div>
            <div class="story_tag">`;

            image.hashtags?.forEach((hashtag)=>{
                 item += `
                 <b><a href="/image/story?search=${hashtag.name}">#${hashtag.name}</a></b>
                 `;
            });

            item += `
            </div>
            <span class="like"><b id="storyLikeCount-${image.id}">${image.likesCount} </b>좋아요</span>
            <div class="sl__item__contents__content">
                <p>${image.caption}</p>
            </div>
            <div id="storyCommentList-${image.id}">`;

                image.comments?.forEach((comment)=>{
                    item += `
                    <div class="sl__item__contents__comment" id="storyCommentItem-${comment.id}">
                        <p>
                            <b>${comment.username} :</b> ${comment.content}
                        </p>`;

                        if(principalId == comment.userId){
                        item += `
                            <button onclick="deleteComment(${comment.id})">
                                <i class="fas fa-times"></i>
                            </button>
                        `;
                        }
                    item += `
                    </div>`;
                });

            item += `
            </div>
            <form class="sl__item__input" action="javascript:addComment(${image.id})">
                <input type="text" placeholder="댓글 달기..." id="storyCommentInput-${image.id}" />
                <button>게시</button>
            </form>
        </div>
    </div>
    `;
    return item;
}

// (2) 스토리 스크롤 페이징하기
$(window).scroll(() => {
    const checkNum = $(window).scrollTop() - ($(document).height()-$(window).height());

    if(checkNum < 1 && checkNum > -1){
        page++;
        storyLoad();
    }
});


// (3) 좋아요, 안좋아요
function toggleLike(imageId) {
	let likeIcon = $(`#storyLikeIcon-${imageId}`);
	if (likeIcon.hasClass("far")) {
	    $.ajax({
            type:"post",
            url:`/api/image/${imageId}/likes`,
            dataType:"json"
        }).done(res=>{
            const likesCountStr = $(`#storyLikeCount-${imageId}`).text();
            const likesCount = Number(likesCountStr) + 1;
            $(`#storyLikeCount-${imageId}`).text(likesCount);

            likeIcon.addClass("fas");
            likeIcon.addClass("active");
            likeIcon.removeClass("far");
        }).fail(error=>{
            console.log("오류",error);
        });
	} else {
        $.ajax({
            type:"delete",
            url:`/api/image/${imageId}/likes`,
            dataType:"json"
        }).done(res=>{
             const likesCountStr = $(`#storyLikeCount-${imageId}`).text();
             const likesCount = Number(likesCountStr) - 1;
             $(`#storyLikeCount-${imageId}`).text(likesCount);

            likeIcon.removeClass("fas");
            likeIcon.removeClass("active");
            likeIcon.addClass("far");
        }).fail(error=>{
           console.log("오류",error);
        });
	}
}

// (4) 댓글쓰기
function addComment(imageId) {

	let commentInput = $(`#storyCommentInput-${imageId}`);
	let commentList = $(`#storyCommentList-${imageId}`);

	let data = {
	    imageId,
		content: commentInput.val()
	}

	if (data.content === "") {
		alert("댓글을 작성해주세요!");
		return;
	}

	$.ajax({
        type:"post",
        url:"/api/comment",
        data:JSON.stringify(data),
        contentType:"application/json;charset=utf-8",
        dataType:"json"
    }).done(res=>{
        const {data} = res;

        const content = `
              <div class="sl__item__contents__comment" id="storyCommentItem-${data.id}">
                <p>
                  <b>${data.username} :</b>
                  ${data.content}
                </p>
                <button onclick="deleteComment(${data.id})"><i class="fas fa-times"></i></button>
              </div>
        `;
        commentList.prepend(content);
    }).fail(error=>{
       alert(error.responseJSON.data.content)
    });


	commentInput.val(""); // 댓글입력창 비우기
}

// (5) 댓글 삭제
function deleteComment(commentId) {
    $.ajax({
        type:"delete",
        url:`/api/comment/${commentId}`,
        dataType:"json"
    }).done(res=>{
        $(`#storyCommentItem-${commentId}`).remove();
    }).fail(error=>{
       console.log("오류",error);
    });
}







