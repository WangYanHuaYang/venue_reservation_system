const form=layui.form

form.on('submit(register)',function (data) {
    registerUser(data.field)
    clearForm(form,'register-form')
    return false
})
form.verify({
    username: function(value, item){ //value：表单的值、item：表单的DOM对象
        if(!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)){
            return '用户名不能有特殊字符'
        }
        if(/(^\_)|(\__)|(\_+$)/.test(value)){
            return '用户名首尾不能出现下划线\'_\''
        }
        if(/^\d+\d+\d$/.test(value)){
            return '用户名不能全为数字'
        }
    },
    pass: [//数组的两个值分别代表：[正则匹配、匹配不符时的提示文字]
        /^[\S]{6,12}$/
        ,'密码必须6到12位，且不能出现空格'
    ],
    repass: function(value,item){
        if (value != $('#password').val()){
            return '确认密码与密码必须相同'
        }
    }
})
function registerUser(form){
    form.password=hex_md5(form.password)
    $.ajax({
        url: '/sys-user/updateSysUser',
        type: 'post',
        async:false,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(form),
        success: function (result) {
            let code=1
            if (result.code!=0){
                code=2
            }else {
                window.location.href = "/login/school_register.html"
            }
            layer.msg(result.message, {
                icon: code, //1 成功 2 失败 3 异常
                time: 1000 //2秒关闭（如果不配置，默认是3秒）
            })
        }
    })
}