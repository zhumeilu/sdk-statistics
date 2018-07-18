<#assign basePath = request.contextPath/>
<script xmlns="http://www.w3.org/1999/html">
    var basePath = '${basePath}'
</script>
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
<form class="layui-form" style="width:80%;" id="userAddForm" method="post">
	<input type="hidden" name="id" value="${(role.id)!}">
    <blockquote class="layui-elem-quote quoteBox">
        <form class="layui-form">
            <div class="layui-inline">
                <a class="layui-btn layui-btn-normal addNews_btn" lay-submit lay-filter="addPermission">添加</a>
            </div>
            <#--<div class="layui-inline">-->
                <#--<a class="layui-btn layui-btn-danger layui-btn-normal delAll_btn">批量删除</a>-->
            <#--</div>-->
        </form>
    </blockquote>
    <table class="layui-table">
        <thead>
            <tr>
                <th style="text-align:center"><input type="checkbox" lay-skin='primary' ></th>
                <th style="text-align:center">id</th>
                <th style="text-align:center">名称</th>
                <th style="text-align:center">类型</th>
            </tr>
        </thead>
        <tbody>
            <#list permissionList as permission>
            <tr>
                <td align="center"><input type='checkbox' class="permissionCheckBox" data-id="${permission.id}" <#if permisisonIds?seq_contains(permission.id)>checked</#if> lay-skin='primary'></td>
                <td align="center">${permission.id}</td>
                <td align="center">${permission.name}</td>
                <td align="center">${permission.type}</td>
            </tr>
            </#list>
        </tbody>
    </table>
</form>
<script type="text/javascript" src="${basePath}/layui/layui.js"></script>
<script>
    layui.use(['form','layer'],function(){
        var form = layui.form
        layer = parent.layer === undefined ? layui.layer : top.layer,
                $ = layui.jquery;

        form.on("submit(addPermission)",function(data){
            newsId = [];
            $(".layui-form-checked").each(function(index,domEle){
                var id =  $(domEle).prev('.permissionCheckBox').data("id");
                newsId.push(id);
            })
//            var checkStatus = table.checkStatus('newsListTable'),
//                    data = checkStatus.data,
//                    newsId = [];
//            if(data.length > 0){
//                for (var i in data) {
//                    newsId.push(data[i].id);
//                }
//            }
            console.log(newsId);
//            return;
            //弹出loading
            var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
            // return false;
            // 实际使用时的提交信息
            $.post(basePath+"/role/rolePermission",
                    {
                        id:$("input[name=id]").val(),
                        permissionids:newsId
                    },
                    function(data){
                        console.log(data);
                        if(data.success){
                            top.layer.close(index);
                            top.layer.msg(data.msg,{
                                icon:1,
                                time:2000
                            },function () {
                                layer.closeAll("iframe");
                                //刷新父页面
//                                parent.location.reload();
                            });

                        }else{
                            top.layer.close(index);
                            top.layer.msg(data.msg,{
                                icon:2,
                                time:2000
                            });
                            // layer.closeAll("iframe");
                            // //刷新父页面
                            // parent.location.reload();
                        }
                    },'json')
            return false;
        })
        <#--var tableIns = table.render({-->
            <#--elem: '#newsList',-->
            <#--url : '${basePath}/permission/list',-->
            <#--cellMinWidth : 95,-->
            <#--page : true,-->
            <#--height : "full-125",-->
            <#--limit : 20,-->
            <#--limits : [10,15,20,25],-->
            <#--id : "newsListTable",-->
            <#--cols : [[-->
                <#--{field: "id", fixed:"left", width:50,templet:function(d){-->
<#--//                    var permissionIds = [1];-->
<#--//                    if(permissionIds.contains(d.permissionId){-->
<#--//                        return "<input type='checkbox' checked lay-skin='primary'>"-->
<#--//                    }else{-->
<#--//                        return "<input type='checkbox' lay-skin='primary'>"-->
<#--//                    }-->
                    <#--return "<input type='checkbox' checked lay-skin='primary'>"-->
                <#--}},-->
                <#--{field: 'id', title: 'ID', width:60, align:"center"},-->
                <#--{field: 'name', title: '名称'},-->
                <#--{field: 'type', title: '类型', align:'center',templet:function(d){-->
                    <#--if(d.type==1)-->
                        <#--return '目录'-->
                    <#--else if(d.type==2)-->
                        <#--return '菜单'-->
                    <#--else if(d.type==3)-->
                        <#--return '按钮'-->
                <#--}},-->
                <#--{field: 'permissionValue', title: '权限值',  align:'center',},-->
                <#--{field: 'uri', title: '路径',  align:'center',},-->
                <#--{field: 'status', title: '状态',  align:'center',templet:function(d){-->
                    <#--if(d.type==0)-->
                        <#--return '禁止'-->
                    <#--else if(d.type==1)-->
                        <#--return '正常'-->
                <#--}},-->
                <#--{field: 'pid', title: '上级',  align:'center',},-->

            <#--]]-->
        <#--});-->

        //转换静态表格
//        table.init('newsList', {
//            height: 315 ,
//            id:'newsListTable'
//        });


//        $(".addNews_btn").on("click",function(){
//            var checkStatus = table.checkStatus('newsListTable'),
//                    data = checkStatus.data,
//                    newsId = [];
//            if(data.length > 0){
//                for (var i in data) {
//                    newsId.push(data[i].id);
//                }
//
//            }else{
//                layer.msg("请输入搜索的内容");
//            }
//        });
    })
</script>
</body>
</html>