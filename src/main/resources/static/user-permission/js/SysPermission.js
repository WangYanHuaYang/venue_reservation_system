// 弹出框类型 1-新增 2-修改
var modelType=1;
var tip_index = 0;
var form = layui.form;
var layer = layui.layer;
$(document).ready(function(){
    initDeptTreeTable()
    getParentPermission();
    $('#addPermission').click(function(){
        closeModel()
        form.render('select', 'add-permission')
        layer.open({
            type: 1,
            area: ['500px', '450px'],
            content: $('#add-permission'),
            btn: ['保存', '关闭'],
            yes: function(){
                addPermission()
            },
            btn2: function() {
                layer.closeAll();
            },
            title: '新增权限'
        });
    })
    $("table").on("mouseover", "td", function () {
        if (this.offsetWidth < this.scrollWidth) { //判断文本是否超出
            var content = $(this).text(); //获取文案
            if (content) {
                tip_index = layer.tips("<div class='noticetishi'>"+content+"</div>", this, {
                    time: 0,
                    tips: [3, '#009688'],
                    area: ['200px', 'auto']
                });
            }
        }
    })
    $("table").on("mouseleave", "td", function () {
        layer.close(tip_index);
    })
    $(document).on("click", ".update_btn", function() {
        id = $(this).attr("upd-id");
        getPermissionById(id)
    });
    $(document).on("click", ".delete-btn", function() {
        var id = $(this).attr("del-id");
        layer.confirm('确定要删除吗？', {
            btn : [ '确定', '取消' ]
        }, function() {
            delet(id)
            layer.close(1);
        });
    });
})

// 选择图标css
function selectCss() {
    layer.open({
        type: 2,
        title: "样式",
        area: ['800px', '400px'],
        maxmin: true,
        shadeClose: true,
        content: ['../images/icon.html']
    });
}
//新增权限
function addPermission(){
    $.ajax({
        url: '/sys-permission/saveSysPermission',
        type: 'put',
        async:true,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({
            parentId:$('#parentMenu').val(),
            permissionName:$('#permissionName').val(),
            permissionType:$('#permissionType').val(),
            permissionUrl:$('#permissionUrl').val(),
            permissionCss:$('#permissionCss').val(),
            sort:$('#sort').val()
        }),
        success: function (result) {
            if (result.code==0){
                initDeptTreeTable()
                form.render(null, 'table')
                layer.closeAll();
                getParentPermission()
            }else {
                layer.msg(result.message, {
                    shift: -1,
                    time: 1000
                });
            }
        }
    });
}
//删除按钮
function delet(id){
    $.ajax({
        type : 'delete',
        url : '/sys-permission/deleteSysPermission',
        data:{
            id:id
        },
        success : function(data) {
            if (data.code==0){
                layer.msg("删除成功")
                initDeptTreeTable()
                getParentPermission()
            }else {
                layer.msg(data.message, {
                    shift: -1,
                    time: 1000
                });
            }
        }
    });
}
//修改按钮
function getPermissionById(id){
    $.ajax({
        url: '/sys-permission/selectSysPermissionById?id='+id,
        type: 'POST',
        async:false,
        success: function (result) {
            if (result.code==0){
                form.val("add-permission", {
                    "parentMenu": result.context.result.parentId,
                    "permissionName": result.context.result.permissionName,
                    "permissionType": result.context.result.permissionType,
                    "permissionUrl": result.context.result.permissionUrl,
                    "permissionCss": result.context.result.permissionCss,
                    "sort":result.context.result.sort
                })
                form.render('select')
                $('#permissionName').attr('upd-id',result.context.result.id)
                $('#cssImg').attr('class','fa '+result.context.result.permissionCss)
            }else {
                layer.msg(result.message, {
                    shift: -1,
                    time: 1000
                });
            }
        }
    });
    layer.open({
        type: 1,
        area: ['500px', '450px'],
        content: $('#add-permission'),
        btn: ['保存', '关闭'],
        yes: function() {
            updat($('#permissionName').attr('upd-id'))
            layer.closeAll();
        },
        btn2: function() {
            layer.closeAll();
        },
        title: '修改权限'
    });
}
// 修改权限
function updat(id){
    console.info($('#permissionCss').val())
    $.ajax({
        url: '/sys-permission/updateSysPermission',
        type: 'POST',
        async:true,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({
            id:id,
            parentId:$('#parentMenu').val(),
            permissionName:$('#permissionName').val(),
            permissionType:$('#permissionType').val(),
            permissionUrl:$('#permissionUrl').val(),
            permissionCss:$('#permissionCss').val(),
            sort:$('#sort').val()
        }),
        success: function (result) {
            if (result.code==0){
                initDeptTreeTable()
                var form = layui.form;
                form.render(null, 'table')
                getParentPermission()
            }else {
                layer.msg(result.message, {
                    shift: -1,
                    time: 1000
                });
            }
        }
    });
}
//获取所有菜单权限
function getParentPermission(){
    $.ajax({
        url: '/sys-permission/selectSysPermissions?pageNum=1&pageSize=9999',
        type: 'post',
        async:false,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({permissionType:1}),
        success: function (result) {
            if (result.code==0){
                $('#parentMenu').empty()
                $('#parentMenu').append('<option value="0">根目录</option>')
                $.each(result.context.resultSet.records,function(index,item){
                    $('#parentMenu').append('<option value="'+item.id+'">'+item.permissionName+'</option>')
                })
                form.render('select')
            }else {
                layer.msg(result.message, {
                    shift: -1,
                    time: 1000
                });
            }
        }
    });
}
// 初始化渲染表格
function initDeptTreeTable() {
    $('#deptTreeTable').bootstrapTreeTable({
        id: 'id', // 选取记录返回的值
        code: 'id', // 用于设置父子关系
        parentCode: 'parentId', // 用于设置父子关系
        type: "POST", // 请求数据的ajax类型
        url: '/sys-permission/getSysPermissions', // 请求数据的ajax的url
        ajaxParams: JSON.stringify({}), // 请求数据的ajax的data属性
        expandColumn: 0, // 在哪一列上面显示展开按钮
        expandAll : false,
        striped: false, // 是否各行渐变色
        columns: [
            {
                title: '名称',
                field: 'permissionName',
                width:'30%'
            },
            {
                title: '图标样式',
                field: 'permissionCss',
                width:'10%',
                formatter: function(value,item, index) {
                    return '<i class="fa '+value.permissionCss+'"></i>';
                }
            },
            {
                title: '页面链接',
                field: 'permissionUrl',
                width:'30%'
            },
            {
                title: '权限类型',
                field: 'permissionType',
                width:'10%',
                formatter: function(value,item, index) {
                    if (value.permissionType == '1') {
                        return '<span class="layui-badge-rim">菜单</span>';
                    }
                    else if (value.permissionType == '2') {
                        return '<span class="layui-badge-rim">按钮</span>';
                    }
                }
            },
            {
                title: '操作',
                field: 'buttonItem',
                align: "center",
                width:'20%',
                formatter: function(value,row, index) {
                    var actions = [];
                    actions.push('<button type="button" title="修改" upd-id="' + value.id + '" class="update_btn layui-btn layui-btn-xs">修改</button>');
                    actions.push('<button type="button" title="删除" del-id="' + value.id + '" class="delete-btn layui-btn layui-btn-danger layui-btn-xs">删除</button>');
                    return actions.join('');
                }

            }
        ], // 设置列 onclick="del(' + item.id + ')"
        expanderExpandedClass : 'fa fa-chevron-up',// 展开的按钮的图标
        expanderCollapsedClass : 'fa fa-chevron-down'// 缩起的按钮的图标
    });

}
// 模态框内容清空
function closeModel(){
    $('#parentMenu').val('0')
    $('#permissionName').val('')
    $('#permissionType').val('0')
    $('#permissionUrl').val('')
    $('#cssImg').removeClass()
    $('#permissionName').removeAttr('upd-id')
}