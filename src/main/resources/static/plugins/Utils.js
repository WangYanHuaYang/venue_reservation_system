// 构建树形结构
function setTreeData(arr,custom) {
    let map = {} // 构建map
    arr.forEach(i => {
        custom(i)
        map[i.id] = i // 构建以area_id为键 当前数据为值
    })

    let treeData = []
    arr.forEach(child => {
        const mapItem = map[child.parentId] // 判断当前数据的parent_id是否存在map中

        if (mapItem) { // 存在则表示当前数据不是最顶层数据

            // 注意: 这里的map中的数据是引用了arr的它的指向还是arr，当mapItem改变时arr也会改变,踩坑点
            (mapItem.children || ( mapItem.children = [] )).push(child)// 这里判断mapItem中是否存在children, 存在则插入当前数据, 不存在则赋值children为[]然后再插入当前数据
        } else { // 不存在则是组顶层数据
            treeData.push(child)
        }
    })

    return treeData;
}
//获取树的id
function getTreeId(arr){
    let ids=[]
    arr.forEach(i =>{
        ids.push(i.id)
        if (i.children!=null){
            let backIds=getTreeId(i.children)
            ids=ids.concat(backIds)
        }
    })
    return ids
}
//清空表单
function clearForm(formObject,domId){
    $('#'+domId)[0].reset()
    formObject.render()
    return false
}
//设置Chackbox选项
function setChackbox(obj,arr){
    arr.forEach(i =>{
        obj['like['+i+']']=true
    })
    return obj;
}
//检查是否登录
function chackSession(){
    if (sessionStorage.getItem('login-bean')==null||sessionStorage.getItem('login-bean')==''){
        layer.msg('请先登录', {
            shift: -1,
            time: 1000
        }, function () {
            window.location.href = "/login/index.html"
        });
    }else {
        return JSON.parse(sessionStorage.getItem('login-bean'));
    }
}
