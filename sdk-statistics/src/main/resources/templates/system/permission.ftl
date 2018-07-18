<#assign basePath = request.contextPath/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>文章列表--layui后台管理模板 2.0</title>
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="format-detection" content="telephone=no">
	<link rel="stylesheet" href="${basePath}/layui/css/layui.css" media="all" />
	<link rel="stylesheet" href="${basePath}/css/public.css" media="all" />
</head>
<body class="childrenBody">
<form class="layui-form">
	<blockquote class="layui-elem-quote quoteBox">
		<form class="layui-form">
			<div class="layui-inline">
				<div class="layui-input-inline">
					<input type="text" class="layui-input searchVal" placeholder="请输入搜索的内容" />
				</div>
				<a class="layui-btn search_btn" data-type="reload">搜索</a>
			</div>
			<div class="layui-inline">
				<a class="layui-btn layui-btn-normal addNews_btn">添加权限</a>
			</div>
			<div class="layui-inline">
				<a class="layui-btn layui-btn-danger layui-btn-normal delAll_btn">批量删除</a>
			</div>
		</form>
	</blockquote>
	<table id="newsList" lay-filter="newsList"></table>
	<!--审核状态-->
	<script type="text/html" id="newsStatus">
		{{#  if(d.newsStatus == "1"){ }}
		<span class="layui-red">等待审核</span>
		{{#  } else if(d.newsStatus == "0"){ }}
		<span class="layui-blue">已存草稿</span>
		{{#  } else { }}
			审核通过
		{{#  }}}
	</script>

	<!--操作-->
	<script type="text/html" id="newsListBar">
		<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
		<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del">删除</a>
		<a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="look">预览</a>
	</script>
</form>
<script type="text/javascript" src="${basePath}/layui/layui.js"></script>
<script>
    layui.use(['form','layer','laydate','table','laytpl'],function(){
        var form = layui.form,
                layer = parent.layer === undefined ? layui.layer : top.layer,
                $ = layui.jquery,
                laydate = layui.laydate,
                laytpl = layui.laytpl,
                table = layui.table;

        //新闻列表
        var tableIns = table.render({
            elem: '#newsList',
            url : 'permission/list',
            cellMinWidth : 95,
            page : true,
            height : "full-125",
            limit : 20,
            limits : [10,15,20,25],
            id : "newsListTable",
            cols : [[
                {type: "checkbox", fixed:"left", width:50},
                {field: 'id', title: 'ID', width:60, align:"center"},
                {field: 'name', title: '名称'},
                {field: 'type', title: '类型', align:'center',templet:function(d){
                    if(d.type==1)
                        return '目录'
                    else if(d.type==2)
                        return '菜单'
                    else if(d.type==3)
                        return '按钮'
                }},
                {field: 'permissionValue', title: '权限值',  align:'center',},
                {field: 'uri', title: '路径',  align:'center',},
                {field: 'status', title: '状态',  align:'center',templet:function(d){
                    if(d.type==0)
                        return '禁止'
                    else if(d.type==1)
                        return '正常'
                }},
                {field: 'pid', title: '上级',  align:'center',},
                {field: 'createDate', title: '创建时间', align:'center'},
                {title: '操作', width:250, templet:'#newsListBar',fixed:"right",align:"center"}
            ]]
        });

        //是否置顶
        form.on('switch(newsTop)', function(data){
            var index = layer.msg('修改中，请稍候',{icon: 16,time:false,shade:0.8});
            setTimeout(function(){
                layer.close(index);
                if(data.elem.checked){
                    layer.msg("置顶成功！");
                }else{
                    layer.msg("取消置顶成功！");
                }
            },500);
        })

        //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
        $(".search_btn").on("click",function(){
            if($(".searchVal").val() != ''){
                table.reload("newsListTable",{
                    page: {
                        curr: 1 //重新从第 1 页开始
                    },
                    where: {
                        key: $(".searchVal").val()  //搜索的关键字
                    }
                })
            }else{
                layer.msg("请输入搜索的内容");
            }
        });

        //添加文章
        function addNews(edit){
            var id = "";
            if(edit){
                id = edit.id;
            }
            var index = layui.layer.open({
                title : "添加权限",
                type : 2,
                content : "permission/add?id="+id,
                method:'get',
                success : function(layero, index){
                    var body = layui.layer.getChildFrame('body', index);
                    // if(edit){
                    //     body.find(".userName").val(edit.username);  //登录名
                    //     body.find(".userEmail").val(edit.email);  //邮箱
                    //     body.find(".userSex input[value="+edit.sex+"]").prop("checked","checked");  //性别
                    //     body.find(".userGrade").val(edit.userGrade);  //会员等级
                    //     body.find(".userStatus").val(edit.userStatus);    //权限状态
                    //     body.find(".userDesc").text(edit.userDesc);    //权限简介
                    //     form.render();
                    // }
                    setTimeout(function(){
                        layui.layer.tips('点击此处返回权限列表', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    },500)
                }
            })
            layui.layer.full(index);
            //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
            $(window).on("resize",function(){
                layui.layer.full(index);
            })
        }
        $(".addNews_btn").click(function(){
            addNews();
        })

        //批量删除
        $(".delAll_btn").click(function(){
            var checkStatus = table.checkStatus('newsListTable'),
                    data = checkStatus.data,
                    newsId = [];
            if(data.length > 0) {
                for (var i in data) {
                    newsId.push(data[i].id);
                }
                layer.confirm('确定删除选中的权限？', {icon: 3, title: '提示信息'}, function (index) {
                    $.post("permission/batchDelete",{
                        ids : newsId  //将需要删除的newsId作为参数传入
                    },function(data){
                        tableIns.reload();
                        layer.close(index);
                    })
                })
            }else{
                layer.msg("请选择需要删除的文章");
            }
        })

        //列表操作
        table.on('tool(newsList)', function(obj){
            var layEvent = obj.event,
                    data = obj.data;

            if(layEvent === 'edit'){ //编辑
                addNews(data);
            } else if(layEvent === 'del'){ //删除
                layer.confirm('确定删除此权限？',{icon:3, title:'提示信息'},function(index){
                    $.post("permission/delete",{
                        id : data.id  //将需要删除的newsId作为参数传入
                    },function(data){
                        tableIns.reload();
                        layer.close(index);
                    })
                });
            } else if(layEvent === 'look'){ //预览
                layer.alert("此功能需要前台展示，实际开发中传入对应的必要参数进行文章内容页面访问")
            }
        });

    })
</script>
</body>
</html>