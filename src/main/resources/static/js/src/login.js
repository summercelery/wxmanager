layui.use(['form', 'layer'], function () {
    var form = layui.form
        , layer = layui.layer;
    $('#login').keydown(function (e) {
        if (e.which == 13) {
            $("#fshLogin").trigger("click");
        }
    });
    //监听提交
    $('#fshLogin').click(function () {
        var loginName = $.trim($("#loginName").val());
        var password = $.trim($("#password").val());
        var remember = $('#remember').is(':checked');
        if(loginName==""){
            layer.msg('请输入账号');
            return;
        }
        if(password==""){
            layer.msg('请输入密码');
            return;
        }

        ajax({
            url: '/user/login',
            type: 'POST',
            dataType: 'json',
            data: {
                loginName: loginName,
                password: password,
                rememberMe:remember
            },
            success: function (result) {
                if (result.success) {
                    if ($('#remember').is(':checked')) {
                        localStorage.setItem('loginName', loginName);
                        localStorage.setItem('password', password);
                    } else {
                        localStorage.clear('loginName');
                        localStorage.clear('password');
                    }
                    localStorage.setItem('nickname', result.data?result.data:"管理员");
                    window.location.href = "/views/index.html";
                }else{
                    layer.msg(result.msg || "登录失败");
                }
            },
            error: function () {
                layer.msg("登录失败");
            }
        })
    })
});

//判断是否存在过用户
if (localStorage.getItem('loginName')) {
    $("#remember").attr("checked", true);
    $("#loginName").val(localStorage.getItem('loginName'));
    $("#password").val(localStorage.getItem('password'))
}
//百度统计
var _hmt=_hmt||[];(function(){var hm=document.createElement("script");hm.src="https://hm.baidu.com/hm.js?f07686635800b1dc0587d91cd81bf3b0";var s=document.getElementsByTagName("script")[0];s.parentNode.insertBefore(hm,s)})();
(function(){var bp=document.createElement("script");var curProtocol=window.location.protocol.split(":")[0];if(curProtocol==="https"){bp.src="https://zz.bdstatic.com/linksubmit/push.js"}else{bp.src="http://push.zhanzhang.baidu.com/push.js"}var s=document.getElementsByTagName("script")[0];s.parentNode.insertBefore(bp,s)})();