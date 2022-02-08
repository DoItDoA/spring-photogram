function deleteAlarm(notificationId) {
    $.ajax({
        type:"delete",
        url:`/api/notification/${notificationId}`,
        dataType:"json"
    }).done(res=>{
        $(`#notification-${notificationId}`).remove();
    }).fail(error=>{
       console.log("오류",error);
    });
}

