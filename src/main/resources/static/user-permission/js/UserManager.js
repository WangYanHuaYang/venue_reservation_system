const layer=layui.layer
const form=layui.form
const table=layui.table
const tree=layui.tree
const laypage = layui.laypage
var roles=[]

getUserTree()
getParentUser()
getRole()
makeTable(1,10)
function makeTable(mpageNum, mpageSize){
    table.render({
        elem: '#users',
        url:'/sys-user/selectSysUsers?pageNum=' + mpageNum + '&pageSize=' + mpageSize,
        request: {
            pageName: 'pageNum',
            limitName: 'pageSize'
        },
        parseData: function(res){ //res 即为原始返回的数据
            return {
                "code": res.code, //解析接口状态
                "msg": res.message, //解析提示文本
                "count": res.context.resultSet.total, //解析数据长度
                "data": res.context.resultSet.records //解析数据列表
            };
        },
        loadingL: true,
        where:{},
        method:'POST',
        contentType: 'application/json',
        cols:[[
            {field: 'id', title: 'ID', hide: true, fixed: 'left'},
            {field: 'userName', title: '用户名', fixed: 'center'},
            {field: 'phoneNumber', title: '联系方式', fixed: 'left'},
            {field: 'email', title: '邮箱', fixed: 'left'},
            {field: 'organization', title: '团队名称', fixed: 'left'},
            {field: 'userLevel', title: '用户层级', fixed: 'left'},
            {title: '操作', fixed: 'center',toolbar: '#tool-bar'}

        ]],
        done: function(res, curr, count) {
            if(count > 0) {
                laypage.render({
                    elem: 'pageList',
                    count: count,
                    curr: mpageNum //设定初始在第 几 页
                    ,
                    layout: ['count', 'prev', 'page', 'next', 'skip'],
                    jump: function(obj, first) {
                        //首次不执行
                        if(!first) {
                            makeTable(obj.curr, obj.limit)
                        }
                    }
                })
            }
        }
    })
}
//监听工具条
table.on('tool(users)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
    var data = obj.data; //获得当前行数据
    var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）

    if(layEvent === 'del'){ //删除
        layer.confirm('确定删除 <span class="layui-badge">'+data.userName+'</span> 么',{icon:2,title:'删除'}, function(index){
            delUser(data.id)
            layer.close(index);
            //向服务端发送删除指令
        })
    } else if(layEvent === 'upd'){ //编辑
        getUserById(data.id)
    }
    else if(layEvent === 'reset'){ //重置密码
        resetPwd(data.id)
    }
})

$('#add-user').click(function(){
    layer.open({
        type: 1,
        area: ['700px', '500px'],
        content: $('#add-user-form'),
        cancel: function(index,layero){
            clearForm(form,'add-user-form')
            layer.close(index)
            return false
        },
        title: '新增角色'
    })
    form.on('submit(save)',function (data) {
        addUser(data.field,null)
        clearForm(form,'add-user-form')
        return false
    })
})
$('#add-invitation-code').click(function(){
    var index=layer.open({
        type: 1,
        area: ['700px', '500px'],
        content: $('#inv-user-form'),
        cancel: function(index,layero){
            clearForm(form,'inv-user-form')
            layer.close(index)
            return false
        },
        title: '生成邀请码'
    })
    form.on('submit(save2)',function (data) {
        addUser(data.field,1)
        clearForm(form,'inv-user-form')
        layer.close(index);
        return false
    })
})

$('#close').click(function (data) {
    layer.closeAll();
    clearForm(form,'add-user-form')
})

$('#find-name').click(function(){
    table.reload('users',{
        where:{
            userName:$('#findUserName').val()
        }
    })
})

form.on('select', function(data){
    var level=data.value.split('-')[1]
    $('#userLevel').val(parseInt(level)+1)
})

form.on('checkbox', function(data){
    if (data.elem.checked){
        roles.push(data.value)
    }else {
        roles=roles.filter(function(item){
            return item != data.value
        })
    }
})

function getUserTree(){
    $.ajax({
        url: '/sys-user/sysUsersTree',
        type: 'post',
        async:false,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({}),
        success: function (result) {
            console.info(result)
            console.info(setTreeData(result,function(i){
                i.title=i.userName
            }))
            tree.render({
                id:'user-tree',
                elem:'#user-tree',
                data:setTreeData(result,function(i){
                    i.title=i.userName
                    i.parentId=i.parentsId
                })
            })
        }
    })
}

function getUserById(id){
    $.ajax({
        url: '/sys-user/selectSysUserById?id='+id,
        type: 'post',
        async:false,
        success: function (result) {
            let code=1
            if (result.code!=0){
                code=2
                layer.msg(result.message, {
                    icon: code, //1 成功 2 失败 3 异常
                    time: 1000 //2秒关闭（如果不配置，默认是3秒）
                })
            }else {
                var f={
                    userName:result.context.result.userName,
                    email:result.context.result.email,
                    organization:result.context.result.organization,
                    phoneNumber:result.context.result.phoneNumber,
                    password:'******',
                    parentsId:result.context.result.parentsId+'-'+(parseInt(result.context.result.userLevel)-1),
                    userLevel:result.context.result.userLevel,
                    roles:result.context.result.roles
                }
                form.val("add-user-form",setChackbox(f,result.context.result.roles))
                form.render()
                layer.open({
                    type: 1,
                    area: ['700px', '500px'],
                    content: $('#add-user-form'),
                    cancel: function(index,layero){
                        clearForm(form,'add-user-form')
                        layer.close(index)
                        return false
                    },
                    title: '修改角色'
                })
                form.on('submit(save)',function (data) {
                    updUser(data.field,result.context.result.id)
                    clearForm(form,'add-user-form')
                    layer.closeAll();
                    return false
                })
            }
        }
    })
}
//获取所有用户
function getParentUser(){
    $.ajax({
        url: '/sys-user/sysUsersTree',
        type: 'post',
        async:false,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({}),
        success: function (result) {
            $('#parentsId').empty()
            $('#parentsId2').empty()
            $('#parentsId').append('<option value="0-0">根目录</option>')
            $('#parentsId2').append('<option value="0-0">根目录</option>')
            $.each(result,function(index,item){
                $('#parentsId').append('<option value="'+item.id+'-'+item.userLevel+'">'+item.userName+'</option>')
                $('#parentsId2').append('<option value="'+item.id+'-'+item.userLevel+'">'+item.userName+'</option>')
            })
            form.render('select')
        }
    })
}

function getRole(){
    $.ajax({
        url: '/sys-role/selectSysRoles?pageNum=1&pageSize=99999',
        type: 'post',
        async:false,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({}),
        success: function (result) {
            $('#roles').empty()
            $('#roles2').empty()
            $.each(result.context.resultSet.records,function(index,item){
                $('#roles').append('<input type="checkbox" value="'+item.id+'" name="like['+item.id+']" title="'+item.roleName+'">')
                $('#roles2').append('<input type="checkbox" value="'+item.id+'" name="like['+item.id+']" title="'+item.roleName+'">')
            })
            form.render('checkbox')
        }
    })
}

function addUser(form,inv){
    let formData;
    if (inv==1){
        formData = {
            userLevel:form.userLevel,
            parentsId:form.parentsId.split('-')[0],
            roles:roles
        }
    }else {
        formData = {
            email:form.email,
            userName:form.userName,
            organization:form.organization,
            phoneNumber:form.phoneNumber,
            password:hex_md5(form.password),
            userLevel:form.userLevel,
            parentsId:form.parentsId.split('-')[0],
            roles:roles
        }
    }
    $.ajax({
        url: '/sys-user/saveSysUser',
        type: 'put',
        async:false,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(formData),
        success: function (result) {
            let code=1
            if (result.code!=0){
                code=2
            }
            if (inv==1){
                layer.confirm('邀请码： <span class="layui-badge layui-bg-green">'+result.context.InvitationCode+'</span>',{title:'邀请码'}, function(index){
                    layer.close(index);
                })
            }else {
                layer.closeAll();
                table.reload('users',null)
                getUserTree()
                getParentUser()
                layer.msg(result.message, {
                    icon: code, //1 成功 2 失败 3 异常
                    time: 1000 //2秒关闭（如果不配置，默认是3秒）
                })
            }
        },
        error:function(xhr, textStatus, errorThrown){
            layer.msg(xhr.responseText.message, {
                icon: 3, //1 成功 2 失败 3 异常
                time: 1000 //2秒关闭（如果不配置，默认是3秒）
            })
        }
    })
}

function updUser(form,id){
    let formData = {
        id:id,
        email:form.email,
        userName:form.userName,
        organization:form.organization,
        phoneNumber:form.phoneNumber,
        password:form.password,
        userLevel:form.userLevel,
        parentsId:form.parentsId.split('-')[0],
        roles:roles
    }
    $.ajax({
        url: '/sys-user/updateSysUser',
        type: 'post',
        async:false,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(formData),
        success: function (result) {
            let code=1
            if (result.code!=0){
                code=2
            }
            table.reload('users',null)
            getUserTree()
            getParentUser()
            layer.msg(result.message, {
                icon: code, //1 成功 2 失败 3 异常
                time: 1000 //2秒关闭（如果不配置，默认是3秒）
            })
        }
    })
}

function resetPwd(id){
    $.ajax({
        url: '/sys-user/resetPassword',
        type: 'post',
        async:false,
        data: {
            id:id
        },
        success: function (result) {
            let code=1
            if (result.code!=0){
                code=2
            }
            table.reload('users',null)
            getUserTree()
            getParentUser()
            layer.msg(result.message, {
                icon: code, //1 成功 2 失败 3 异常
                time: 1000 //2秒关闭（如果不配置，默认是3秒）
            })
        }
    })
}

function delUser(id){
    $.ajax({
        url: '/sys-user/deleteSysUser?id='+id,
        type: 'delete',
        async:false,
        success: function (result) {
            let code=1
            if (result.code!=0){
                code=2
            }
            table.reload('users',null)
            getUserTree()
            getParentUser()
            layer.msg(result.message, {
                icon: code, //1 成功 2 失败 3 异常
                time: 1000 //2秒关闭（如果不配置，默认是3秒）
            })
        }
    })
}

form.verify({
    username: function(value, item){ //value：表单的值、item：表单的DOM对象
        if(!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)){
            return '用户名不能有特殊字符';
        }
        if(/(^\_)|(\__)|(\_+$)/.test(value)){
            return '用户名首尾不能出现下划线\'_\'';
        }
        if(/^\d+\d+\d$/.test(value)){
            return '用户名不能全为数字';
        }
    }

    //我们既支持上述函数式的方式，也支持下述数组的形式
    //数组的两个值分别代表：[正则匹配、匹配不符时的提示文字]
    ,pass: [
        /^[\S]{6,12}$/
        ,'密码必须6到12位，且不能出现空格'
    ]
})