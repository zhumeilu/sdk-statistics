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
	<input type="hidden" name="id" value="${(user.id)!}">
    <div class="layui-form-item layui-row layui-col-xs12">
        <div class="layui-input-block">
            <#list roleList as role >
            <input type="checkbox" name="roleids" value="${role.id}" title="${role.name}" <#if userRoleIds?seq_contains(role.id)>checked</#if>>
            </#list>
        </div>
    </div>
    <div class="layui-form-item layui-row layui-col-xs12">
        <div class="layui-input-block">
            <button class="layui-btn layui-btn-sm" lay-submit lay-filter="addUser">立即添加</button>
            <button type="reset" class="layui-btn layui-btn-sm layui-btn-primary">取消</button>
        </div>
    </div>
</form>
<script type="text/javascript" src="${basePath}/layui/layui.js"></script>
<script>
    layui.use(['form','layer'],function(){
        var form = layui.form
        layer = parent.layer === undefined ? layui.layer : top.layer,
                $ = layui.jquery;

        form.on("submit(addUser)",function(data){
            //弹出loading
            var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
            // return false;
            // 实际使用时的提交信息
            $.post(basePath+"/user/userRole",
                    $(data.form).serialize(),
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
                                parent.location.reload();
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
            // setTimeout(function(){
            //     // top.layer.close(index);
            //     top.layer.msg("用户添加成功！");
            //     layer.closeAll("iframe");
            //     //刷新父页面
            //     parent.location.reload();
            // },2000);
            return false;
        })

        //格式化时间
        function filterTime(val){
            if(val < 10){
                return "0" + val;
            }else{
                return val;
            }
        }
        //定时发布
        var time = new Date();
        var submitTime = time.getFullYear()+'-'+filterTime(time.getMonth()+1)+'-'+filterTime(time.getDate())+' '+filterTime(time.getHours())+':'+filterTime(time.getMinutes())+':'+filterTime(time.getSeconds());

    })
</script>
</body>
</html>