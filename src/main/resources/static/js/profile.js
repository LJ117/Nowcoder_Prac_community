$(function () {
    $(".follow-btn").click(follow);
});

function follow() {
    var btn = this;
    if ($(btn).hasClass("btn-info")) {
        // 关注TA
        $.post(
            CONTEXT_PATH + "/follow",
            // 此时的关注实体是用户
            {"entityType": 3, "entityId": $(btn).prev().val()},
            function (data) {
                data = $.parseJSON(data);
                if (data == 0) {
                    window.location.reload();
                } else {
                    alert(data.msg);

                    window.location.reload();
                }

            }
        );
        // $(btn).text("已关注").removeClass("btn-info").addClass("btn-secondary");
    } else {
        // 取消关注
        $.post(
            CONTEXT_PATH + "/unfollow",
            // 此时的关注实体是用户
            {"entityType": 3, "entityId": $(btn).prev().val()},
            function (data) {
                data = $.parseJSON(data);
                if (data == 0) {
                    window.location.reload();
                } else {
                    alert(data.msg);

                    window.location.reload();
                }

            }
        );
        // $(btn).text("关注TA").removeClass("btn-secondary").addClass("btn-info");
    }
}