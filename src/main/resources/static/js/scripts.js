
jQuery(document).ready(function() {

    $('.page-container form .username, .page-container form .password').keyup(function(){
        $(this).parent().find('.error').fadeOut('fast');
    });

    $("#checkLogin").click(function() {
        login();
    })

    $("#username").bind("keypress",function (event) {
        if (event.keyCode == "13") {
            login()
        }
    });

    $("#password").bind("keypress",function (event) {
        if (event.keyCode == "13") {
            login()
        }
    });

    function login() {
        var username=$("#username").val();
        var password=$("#password").val();

        if(username == '') {
            $(this).find('.error').fadeOut('fast', function(){
                $(this).css('top', '27px');
            });
            $(this).find('.error').fadeIn('fast', function(){
                $(this).parent().find('.username').focus();
            });
            return false;
        }
        if(password == '') {
            $(this).find('.error').fadeOut('fast', function(){
                $(this).css('top', '96px');
            });
            $(this).find('.error').fadeIn('fast', function(){
                $(this).parent().find('.password').focus();
            });
            return false;
        }

        $.post("checkLogin",{username:username, password:password}, function(data,status,xhr){
            if (data.ret == true) {
                window.location.href="/index";
            } else {
                alert("登录失败:" + data.msg);
            }
        })
    }
});
