$(function () {
    $("#uploadForm").submit(upload);
});

function upload() {

    $.ajax({
        url: "http://upload-z2.qiniup.com",
        method: "post",
        // 表单数据不要转化成字符串
        processData: false,
        // 不让 JQuery 设置 上传类型, 浏览器自己处理
        contentType: false,
        data: new FormData($("#uploadForm")[0]),
        success: function (data) {
            if (data && data.code == 0) {
                // 更新头像访问路径
                $.post(
                    CONTEXT_PATH + "/user/header/url",
                    {"fileName": $("input[name='key']").val()},
                    function (data) {
                        data = $.parseJSON(data);
                        if (data.code == 0) {
                            window.location.reload();
                        }else {
                            alert(data.msg);
                        }
                    }
                );
            } else {
                alert("上传失败!");
            }
        }
    });

    // 不用再往下提交了, 避免了这个 form 中没有 action ,而是异步提交的问题
    return false;
}