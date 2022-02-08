const imageId = $("#myStory-imageId").val();
const principalId = $("#principalId").val();
myStoryLoad();
function myStoryLoad() {
    $.ajax({
        url:`/api/image/${imageId}/myStory`,
        dataType:"json"
    }).done(res=>{
        const image = res.data.content[0];
        const storyItem = getMyStoryItem(image);
        $("#myStory").append(storyItem);
    }).fail(error=>{
        console.log("이미지 불러오기 실패",error);
    });
}

function getMyStoryItem(image) {
    let item = `
    <div class="story-list__item">
        <div class="sl__item__header">
            <a href="/user/${image.user.id}">
                <div>
                    <img class="profile-image" src="/upload/${image.user.profileImageUrl}"
                        onerror="this.src='/images/logo.jpg'" />
                </div>
                <div>${image.user.username}</div>
            </a>`;
            if(image.user.id == principalId){
            item += `<button onclick="deleteImage(${image.id})"><i class="far fa-trash-alt"></i></button>`;
            }
        item += `
        </div>
        <div class="sl__item__img" onclick="popup('.modal-image',${image.user.id},${principalId})">
            <form id="myStoryImageForm">
                <input type="file" name="myStoryImageFile" style="display: none;"
                    id="myStoryImageInput" />
            </form>
            <img src="/upload/${image.postImageUrl}" id="myStoryImage" />
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

function popup(obj, pageUserId = null, principalId = null) {
    if(pageUserId !== principalId) {
    console.log(2);
        return;
    }
    console.log(1);
	$(obj).css("display", "flex");
}

function closePopup(obj) {
	$(obj).css("display", "none");
}

function modalImage() {
	$(".modal-image").css("display", "none");
}

function imageUpload(imageId) {

	$("#myStoryImageInput").click();

	$("#myStoryImageInput").on("change", (e) => {
		let f = e.target.files[0];

		if (!f.type.match("image.*")) {
			alert("이미지를 등록해야 합니다.");
			return;
		}
		// 서버에 이미지 전송
		const imageForm = $("#myStoryImageForm")[0];

        // FormData 객체를 이용하면 form 태그의 필드와 그 값을 나타내는 일련의 key/value 쌍을 담을 수 있다.
        const formData = new FormData(imageForm); // 사진만 사용시 이거 사용

        $.ajax({
            type:"put",
            url:`/api/image/${imageId}/imageUrl`,
            data:formData,
            contentType: false, //필수 : x-www-form-urlencoded로 파싱되는 것을 방지
            processData: false, //필수 : contentType을 false로 줬을 때 QueryString 으로 자동으로 설정되는데 그것도 해제
            enctype:"multipart/form-data",
            dataType:"json"
        }).done(res=>{
            // 사진 전송 성공시 이미지 변경
            let reader = new FileReader();
            reader.onload = (e) => {
                $("#myStoryImage").attr("src", e.target.result);
            }
            reader.readAsDataURL(f); // 이 코드 실행시 reader.onload 실행됨.
        }).fail(error=>{
            console.log("오류",error);
        });
	});
}

function deleteImage(imageId) {
    if(confirm("정말로 삭제하시겠습니까?") === true){
        $.ajax({
            type:"delete",
            url:`/api/image/${imageId}/remove`,
            dataType:"json"
        }).done(res=>{
            window.location.href=`/user/${principalId}`;
        }).fail(error=>{
            console.log("오류",error);
        });
    } else {
        return;
    }
}
