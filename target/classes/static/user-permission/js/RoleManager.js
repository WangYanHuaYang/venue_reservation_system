const layer=layui.layer
const form=layui.form
const table=layui.table
const tree=layui.tree
var laypage = layui.laypage

getPermissionTree()
makeTable(1,10)
function makeTable(mpageNum, mpageSize){
    table.render({
        elem: '#roles',
        url:'/sys-role/selectSysRoles?pageNum=' + mpageNum + '&pageSize=' + mpageSize,
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
        method:'POST',
        contentType: 'application/json',
        cols:[[
            {field: 'id', title: 'ID', hide: true, fixed: 'left'},
            {field: 'roleName', title: '角色名', fixed: 'center'},
            {field: 'roleDescription', title: '角色描述', fixed: 'left'},
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
table.on('tool(roles)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
    var data = obj.data; //获得当前行数据
    var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）

    if(layEvent === 'del'){ //删除
        layer.confirm('确定删除 <span class="layui-badge">'+data.roleName+'</span> 么',{icon:2,title:'删除'}, function(index){
            delRole(data.id)
            layer.close(index);
            //向服务端发送删除指令
        });
    } else if(layEvent === 'upd'){ //编辑
        getRoleById(data.id)
    }
})
$('#add-role').click(function(){
    layer.open({
        type: 1,
        area: ['500px', '450px'],
        content: $('#add-role-form'),
        cancel: function(index,layero){
            clearForm(form,'add-role-form')
            layer.close(index)
            return false
        },
        title: '新增角色'
    })
    form.on('submit(save)',function (data) {
        addRole(data.field)
        clearForm(form,'add-role-form')
        layer.closeAll();
        return false
    })
})
$('#close').click(function (data) {
    layer.closeAll();
    clearForm(form,'add-role-form')
})

function getPermissionTree(){
    $.ajax({
        url: '/sys-permission/getSysPermissions',
        type: 'post',
        async:false,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({}),
        success: function (result) {
            tree.render({
                id:'permission-tree',
                elem:'#permission-tree',
                showCheckbox:true,
                data:setTreeData(result,function(i){
                    i.title=i.permissionName
                })
            })
        }
    })
}

function getRoleById(id){
    $.ajax({
        url: '/sys-role/selectSysRoleById?id='+id,
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
                form.val("add-role-form",{
                    'roleName':result.context.result.roleName,
                    'roleDescription':result.context.result.roleDescription,
                })
                tree.setChecked('permission-tree', result.context.result.permissions)
                layer.open({
                    type: 1,
                    area: ['500px', '450px'],
                    content: $('#add-role-form'),
                    cancel: function(index,layero){
                        clearForm(form,'add-role-form')
                        layer.close(index)
                        return false
                    },
                    title: '修改角色'
                })
                form.on('submit(save)',function (data) {
                    updRole(data.field,result.context.result.id)
                    clearForm(form,'add-role-form')
                    layer.closeAll();
                    return false
                })
            }
        }
    })
}

function addRole(form){
    let formData = {
        roleName:form.roleName,
        roleDescription:form.roleDescription,
        permissions:getTreeId(tree.getChecked('permission-tree'))
    }
    $.ajax({
        url: '/sys-role/saveSysRole',
        type: 'put',
        async:false,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(formData),
        success: function (result) {
            let code=1
            if (result.code!=0){
                code=2
            }
            table.reload('roles',null)
            layer.msg(result.message, {
                icon: code, //1 成功 2 失败 3 异常
                time: 1000 //2秒关闭（如果不配置，默认是3秒）
            })
        }
    })
}

function updRole(form,id){
    let formData = {
        id:id,
        roleName:form.roleName,
        roleDescription:form.roleDescription,
        permissions:getTreeId(tree.getChecked('permission-tree'))
    }
    $.ajax({
        url: '/sys-role/updateSysRole',
        type: 'post',
        async:false,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(formData),
        success: function (result) {
            let code=1
            if (result.code!=0){
                code=2
            }
            table.reload('roles',null)
            layer.msg(result.message, {
                icon: code, //1 成功 2 失败 3 异常
                time: 1000 //2秒关闭（如果不配置，默认是3秒）
            })
        }
    })
}

function delRole(id){
    $.ajax({
        url: '/sys-role/deleteSysRole?id='+id,
        type: 'delete',
        async:false,
        success: function (result) {
            let code=1
            if (result.code!=0){
                code=2
            }
            table.reload('roles',null)
            layer.msg(result.message, {
                icon: code, //1 成功 2 失败 3 异常
                time: 1000 //2秒关闭（如果不配置，默认是3秒）
            })
        }
    })
}