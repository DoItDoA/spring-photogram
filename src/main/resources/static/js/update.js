// (1) 회원정보 수정
function update(userId, event) {
    event.preventDefault();
    const data = $("#profileUpdate").serialize();  // 사진말고도 캡션등 여러 자료가 있을 시 이거 사용

    $.ajax({
        type:"put",
        url:`/api/user/${userId}`,
        data: data,
        contentType:"application/x-www-form-urlencoded;charset=utf-8",
        dataType:"json"
    }).done(res=>{
        console.log("성공",res);
        location.href=`/user/${userId}`;
    }).fail(error=>{
    if(error.responseJSON.data == null){
       alert(error.responseJSON.message);
    }else{
       alert(JSON.stringify(error.responseJSON.data));
    }
    });
}