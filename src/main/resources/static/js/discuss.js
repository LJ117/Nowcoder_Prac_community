function like(btn, entityType, entityId, entityUserId) {
    $.post(
        CONTEXT_PATH + "/like",
        {"entityType": entityType, "entityId": entityId,"entityUserId":entityUserId},
        function (data) {
            data = $.parseJSON(data);
            if (data.code == 0) {
                //    此次请求成功
                // 获取当前节点的子标签
                $(btn).children("i").text(data.likeCount);
                $(btn).children("b").text(data.likeStatus == 1 ? '已赞' : "赞");
            } else {
                alert(data.msg);
            }
        }
    );

}