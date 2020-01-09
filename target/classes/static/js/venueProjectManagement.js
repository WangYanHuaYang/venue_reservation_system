var venueProjectParentID = ''; //场馆项目父ID
var parentsIdArray = [];
var searchFlage = false; //搜索标识
$(document).ready(function() {
	
	// 初始化内容
	setCookie("searchText", '') //设置搜索文本框 缓存为空
	getVenueProjectParentID();
});
//获取场馆项目父ID
function getVenueProjectParentID() {
	$.ajax({
		url: service_BaseUrl + '/dictionaries/selectDictionariess?pageNum=1&pageSize=9999', // 数据接口URL
		type: 'post', //请求方式 
		dataType: "json",
		contentType: "application/json",
		data: JSON.stringify({}),//json参数
		async: true, //false 同步  true 异步
		cache: false, //缓存数据 false不保存 true保存\n
		success: function(data) { //成功后返回方法 data 返回的数据
			//alert(data)
			if(data.code == 0) {
				var object_array = data.context.resultSet.records;
				for(var i = 0; i < object_array.length; i++) {
					var project_obj = object_array[i];
					if(project_obj.parentId === '0') {
						parentsIdArray.push(project_obj);
					}
				}
				//alert(parentsIdArray.length)
				if(parentsIdArray.length > 0) {
					for(var i = 0; i < parentsIdArray.length; i++) {
						//相等(==)运算比较时，如果一边是字符，一边是数字，会先将字符串转换成数字再进行比较；严格相等(===)则不会进行类型转换，会比较类型是否相等
						var parent_project_obj = parentsIdArray[i];
						if(parent_project_obj.dictionariesName === '场馆项目') {
							venueProjectParentID = parent_project_obj.id;
						}
					}
				}
				//alert('场馆项目父ID = ' + venueProjectParentID);

				loadingTableData(venueProjectParentID, 1, 10, '');
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) { //失败后返回方法 
			layer.msg('网络请求失败,请稍后重试!', {
				icon: 2, //1 成功 2 失败 3 异常
				time: 1000 //2秒关闭（如果不配置，默认是3秒）
			}, function() {
				//location.reload();
			});
		}
	});
}

//加载表格数据
function loadingTableData(mvenueProjectParentID, mpageNum, mpageSize, mdictionariesName) {
	var dictionariesName = '';
	if(searchFlage) {
		dictionariesName = mdictionariesName
	}
	layui.use('table', function() {
		var table = layui.table;
		//alert(pageNum)
		table.render({
			elem: '#test',
			url: service_BaseUrl + '/dictionaries/selectDictionariess?pageNum=' + mpageNum + '&pageSize=' + mpageSize + '',
			method: 'POST', //请求方式
			contentType: 'application/json',
			id: 'projectTable',
			where: {
				"parentId": mvenueProjectParentID,
				"dictionariesName": dictionariesName
			},
			parseData: function(res) { //res 即为原始返回的数据
				//alert(JSON.stringify(res))
				return {
					"code": res.code, //解析接口状态
					"msg": res.message, //解析提示文本
					"count": res.context.resultSet.total, //解析数据长度
					"data": res.context.resultSet.records //解析数据列表
				};
			},
			request: {
				pageName: 'pageNum' //页码的参数名称，默认：page
					,
				limitName: 'pageSize' //每页数据量的参数名，默认：limit
			},
			toolbar: '#toolbarDemo',
			loadingL: true,
			cols: [
				[{
					type: 'radio'
				}, {
					field: 'id',
					width: 200,
					title: 'ID',
					sort: true
				}, {
					field: 'dictionariesName',
					width: 200,
					title: '项目名称'
				}, {
					field: 'createTime',
					width: 200,
					title: '创建时间',
					sort: true
				}, {
					field: 'updateTime',
					width: 200,
					title: '最后更新时间',
					sort: true
				},{
					toolbar: '#barDemo',
					width: 200,
					title: '操作',
					sort: true
				}]
			],
			done: function(res, curr, count) {
				if(count > 0) {
					layui.use('laypage', function() {
						var laypage = layui.laypage;
						//执行一个laypage实例
						laypage.render({
							elem: 'pageList',
							count: count,
							curr: mpageNum //设定初始在第 几 页
								,
							layout: ['count', 'prev', 'page', 'next', 'skip'],
							jump: function(obj, first) {
								//obj包含了当前分页的所有参数，比如：
								//								console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
								//								console.log(obj.limit); //得到每页显示的条数
								//首次不执行
								if(!first) {
									if(getCookie("searchText") != '') {
										searchFlage = true;
									} else {
										searchFlage = false;
									}
									loadingTableData(venueProjectParentID, obj.curr, obj.limit, getCookie("searchText"))
								}
							}
						});
					});
				}
			}
		});
		//头工具栏事件
		table.on('toolbar(test)', function(obj) {
			var checkStatus = table.checkStatus(obj.config.id);
			switch(obj.event) {
				case 'addData'://新增按钮点击事件
					//触发事件
					var active = {
						addProject: function(othis) {
							var type = othis.data('type'),
								text = othis.text();
							layer.open({
								type: 1,
								offset: type, //具体配置参考：http://www.layui.com/doc/modules/layer.html#offset
								id: 'layerDemo' + type, //防止重复弹出
								//								content: ['addProject.html?vppi='+venueProjectParentID+'', 'no'],
								content: $('#dom_AddProject'),
								btn: ['保存', '关闭'],
								btnAlign: 'c',
								yes: function() {
									//请求网路数据 添加项目
									$.ajax({
										type: "put",
										url: service_BaseUrl + '/dictionaries/saveDictionaries',
										contentType: "application/json",
										data: JSON.stringify({
											"dictionariesName": $("#proectName").val(),
											"parentId": venueProjectParentID,
										}),
										dataType: "json",
										success: function(result) {
											if(result.code === 0) {
												layer.closeAll();
												layer.msg('提交成功', {
													icon: 1, //1 成功 2 失败 3 异常
													time: 1000 //2秒关闭（如果不配置，默认是3秒）
												}, function() {
													//刷新页面
													location.reload();
												});
											} else {
												layer.msg(result.message);
											}
										},
										error: function(XMLHttpRequest, textStatus, errorThrown) { //失败后返回方法 
											layer.msg('网络请求失败,请稍后重试!', {
												icon: 2, //1 成功 2 失败 3 异常
												time: 1000 //2秒关闭（如果不配置，默认是3秒）
											}, function() {
												//location.reload();
											});
										}
									});
								},
								btn2: function() {
									layer.closeAll();
								},
								title: '新增项目',
								shade: 0 //不显示遮罩
							});
						}

					};
					var othis = $(this),
						method = othis.data('method');
					active[method] ? active[method].call(this, othis) : '';
					break;
					
				case "searchData":	//搜索按钮点击事件
				
					if($("#searchEditText").val() != '') {
						mpageNum = 1
						setCookie("searchText", $("#searchEditText").val())
					} else {
						setCookie("searchText", '')
						location.reload();
					}
					//表格重载
					table.reload('projectTable', {
						url: service_BaseUrl + '/dictionaries/selectDictionariess?pageNum=' + mpageNum + '&pageSize=' + mpageSize + '',
						where: { //设定异步数据接口的额外参数，任意设
							"parentId": venueProjectParentID,
							"dictionariesName": $("#searchEditText").val()
						},
						done: function(res, curr, count) {
							
							if(count > 0) {
								layui.use('laypage', function() {
									var laypage = layui.laypage;
									//执行一个laypage实例
									laypage.render({
										elem: 'pageList',
										count: count,
										curr: mpageNum, //设定初始在第 几 页
										layout: ['count', 'prev', 'page', 'next', 'skip'],
										jump: function(obj, first) {
											//obj包含了当前分页的所有参数，比如：
											console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
											//console.log(obj.limit); //得到每页显示的条数
											//首次不执行
											if(!first) {
												//判断搜索文本缓存是否有值 如果不等于空 标识执行了搜索操作
												if(getCookie("searchText") != '') {
													searchFlage = true;
												} else {
													searchFlage = false;
												}
												loadingTableData(venueProjectParentID, obj.curr, obj.limit, getCookie("searchText"))
											}
										}
									});
								});
							}
						}
					}); //只重载数据
					break;
			};
		});

		//监听每行工具事件 当前也包括 编辑 删除
		table.on('tool(test)', function(obj) {
			var data = obj.data;
		//	console.log(obj)
			//删除操作
			if(obj.event === 'del') {
				layer.confirm('确定删除行么', function(index) {
					obj.del();
					var projectId = data.id;
					// layer.alert(projectId);
					//请求网路数据 删除项目
					$.ajax({
						type: "delete",
						url: service_BaseUrl + '/dictionaries/deleteDictionaries?id=' + projectId + '',
						contentType: "application/json",
						data: JSON.stringify({}),
						dataType: "json",
						success: function(result) {
							if(result.code === 0) {
								layer.closeAll();
								layer.msg('删除成功', {
									icon: 1, //1 成功 2 失败 3 异常
									time: 1000 //2秒关闭（如果不配置，默认是3秒）
								}, function() {
									//刷新页面
									location.reload();
								});
							} else {
								layer.msg(result.message);
							}
						},
						error: function(XMLHttpRequest, textStatus, errorThrown) { //失败后返回方法 
							layer.msg('网络请求失败,请稍后重试!', {
								icon: 2, //1 成功 2 失败 3 异常
								time: 1000 //2秒关闭（如果不配置，默认是3秒）
							}, function() {
								//location.reload();
							});
						}
					});
				});
				//编辑操作
			} else if(obj.event === 'edit') {
				var data = obj.data;
				$("#proectName").val(data.dictionariesName)
				//触发事件
				var active = {
					editProject: function(othis) {
						var type = othis.data('type'),
							text = othis.text();
						layer.open({
							type: 1,
							offset: type, //具体配置参考：http://www.layui.com/doc/modules/layer.html#offset
							id: 'layerDemo' + type, //防止重复弹出
							//								content: ['addProject.html?vppi='+venueProjectParentID+'', 'no'],
							content: $('#dom_AddProject'),
							btn: ['保存', '关闭'],
							btnAlign: 'c',
							yes: function() {
								//请求网路数据 修改项目
								$.ajax({
									type: "post",
									url: service_BaseUrl + '/dictionaries/updateDictionaries',
									contentType: "application/json",
									data: JSON.stringify({
										"dictionariesName": $("#proectName").val(),
										"id": data.id,
									}),
									dataType: "json",
									success: function(result) {
										if(result.code === 0) {
											layer.closeAll();
											layer.msg('更新成功', {
												icon: 1, //1 成功 2 失败 3 异常
												time: 1000 //2秒关闭（如果不配置，默认是3秒）
											}, function() {
												//刷新页面
												location.reload();
											});
										} else {
											layer.msg(result.message);
										}
									},
									error: function(XMLHttpRequest, textStatus, errorThrown) { //失败后返回方法 
										layer.msg('网络请求失败,请稍后重试!', {
											icon: 2, //1 成功 2 失败 3 异常
											time: 1000 //2秒关闭（如果不配置，默认是3秒）
										}, function() {
											//location.reload();
										});
									}
								});
							},
							btn2: function() {
								layer.closeAll();
							},
							title: '编辑项目',
							shade: 0 //不显示遮罩
						});
					}

				};
				var othis = $(this),
					method = othis.data('method');
				active[method] ? active[method].call(this, othis) : '';

			}
		});

	});

}