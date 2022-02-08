
function searchInfoModalOpen() {
    let search = document.getElementById("search").value;

    if(search.startsWith("#")){
        search = search.substring(1);
        window.location.href=`/image/story?search=${search}`;
        return;
    }

	$(".modal-search").css("display", "flex");
	$.ajax({
        url:`/api/user/${search}`,
        dataType:"json"
    }).done(res=>{
        res.data.forEach((u)=>{
            const item = getSearchModalItem(u);
            $("#searchModalList").append(item);
        });
    }).fail(error=>{
        console.log("팔로우 불러오기 실패",error);
    });
}

function getSearchModalItem(u) {

    let item = `
    <div class="search__item" id="searchModalItem-${u.id}">
        <div class="search__img">
            <img src="/upload/${u.profileImageUrl}" onerror="this.src='/images/logo.jpg'"/>
        </div>
        <a href="/user/${u.id}">
            <div class="search__text">
                <h2>${u.username}</h2>
                <h3>${u.name}</h3>
            </div>
        </a>
    </div>
    `;
    return item;
}

function modalClose() {
	$(".modal-search").css("display", "none");
	location.reload();
}