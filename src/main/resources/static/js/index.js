layui.use('element', function() {
    var $ = layui.jquery
    var element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
    //触发事件
    var active = {
        //在这里给active绑定几项事件，后面可通过active调用这些事件
        tabAdd: function(url, id, name) {
            //新增一个Tab项 传入三个参数，分别是tab页面的地址，还有一个规定的id，对应其标题，是标签中data-id的属性值
            //关于tabAdd的方法所传入的参数可看layui的开发文档中基础方法部分
            element.tabAdd('home-tabs', {
                title: name,
                content: '<iframe id="aaa" data-frameid="' + id + '" scrolling="auto" frameborder="0" src="' + url +
                    '" style="width:100%;height:100%"></iframe>',
                id: id //规定好的id
            })
        },
        tabChange: function(id) {
            //切换到指定Tab项
            element.tabChange('home-tabs', id); //根据传入的id传入到指定的tab项
        },
        tabDelete: function(id) {
            element.tabDelete('home-tabs', id); //删除
        },
        tabRefresh: function(id) { //刷新页面
            $("iframe[data-frameid='" + id + "']").attr("src", $("iframe[data-frameid='" + id + "']").attr("src")) //刷新框架
        }
    };

    //当点击有site-demo-active属性的标签时，即左侧菜单栏中内容 ，触发点击事件
    $('.site-demo-active').on('click', function() {
        var dataid = $(this);

        //这时会判断右侧.layui-tab-title属性下的有lay-id属性的li的数目，即已经打开的tab项数目
        if ($(".layui-tab-title li[lay-id]").length <= 0) {
            //如果比零小，则直接打开新的tab项
            active.tabAdd(dataid.attr("data-url"), dataid.attr("data-id"), dataid.attr("data-title"));
        } else {
            //否则判断该tab项是否以及存在
            var isData = false; //初始化一个标志，为false说明未打开该tab项 为true则说明已有
            $.each($(".layui-tab-title li[lay-id]"), function() {
                //如果点击左侧菜单栏所传入的id 在右侧tab项中的lay-id属性可以找到，则说明该tab项已经打开
                if ($(this).attr("lay-id") == dataid.attr("data-id")) {
                    isData = true;
                }
            })
            if (isData == false) {
                //标志为false 新增一个tab项
                active.tabAdd(dataid.attr("data-url"), dataid.attr("data-id"), dataid.attr("data-title"));
                //添加删除按钮
                $('.layui-tab-title li:last').append('<i class="layui-icon layui-unselect layui-tab-close">ဆ</i>')
                //删除
                $('.layui-tab-title li i').click(function(event) {
                    active.tabDelete($(this).parent().attr('lay-id'))
                });
            }
        }
        //最后不管是否新增tab，最后都转到要打开的选项页面上
        active.tabChange(dataid.attr("data-id"));
    })
    //打开默认页面
    active.tabAdd("html/portalEntry.html", "portalEntry", "首页");
    active.tabChange("portalEntry");
    //监听
    element.on('tab(home-tabs)', function(data) {
        //获取当前切换到的tab栏id
        var tabs_layui_this = $('.layui-tab-title .layui-this').attr('lay-id')
        //判断是否当前已选栏目 防止左侧导航重复操作
        if ($("#nav-left a[data-id="+tabs_layui_this+"]").parent().hasClass('layui-this')==false) {
            //删除当前class&给当前栏目添加 layui-this 类
            $("#nav-left a").parent().removeClass('layui-this')
            $("#nav-left a[data-id="+tabs_layui_this+"]").parent().addClass('layui-this')
            //判断左侧导航的当前栏目是否折叠
            if ($("#nav-left a[data-id="+tabs_layui_this+"]").parent().parent().hasClass('layui-nav-child')) {
                $("#nav-left a[data-id="+tabs_layui_this+"]").parent().parent().parent().addClass('layui-nav-itemed')
            }
        }
    });
});