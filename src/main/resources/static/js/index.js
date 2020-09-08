$(function () {
    $("#publishBtn").click(publish);
});

function publish() {
    $("#publishModal").modal("hide");

    // 获取标题和内容
    // .val() 获取当前 id标签 的 value
    var title = $("#recipient-name").val();
    var content = $("#message-text").val();

    // 发送异步请求(POST)
    $.post(
        CONTEXT_PATH + "/discuss/add",
        {"title": title, "content": content},
        function (data) {
            data = $.parseJSON(data);
            // 在提示框中显示返回消息
            // .text("xxxx") 将该标签的文本内容修改为 "xxxx"
            $("#hintBody").text(data.msg);
            // 显示提示框
            $("#hintModal").modal("show");
            // 2秒后, 自动隐藏提示框
            setTimeout(function () {
                $("#hintModal").modal("hide");
                // 刷新页面 [ 添加成功才刷新, 即 code = 0 ]
                if (data.code == 0) {
                    // 重新加载当前窗口
                    window.location.reload();
                }
            }, 2000);
        }
    );


}