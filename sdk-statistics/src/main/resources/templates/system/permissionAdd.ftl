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
	<input type="hidden" name="id" value="${(permission.id)!}">
	<div class="layui-form-item layui-row layui-col-xs12">
		<label class="layui-form-label">权限名</label>
		<div class="layui-input-block">
			<input type="text" class="layui-input" name="name" value="${(permission.name)!}" lay-verify="required" placeholder="请输入权限名">
		</div>
	</div>
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">权限值</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" name="permissionValue" value="${(permission.permissionValue)!}" placeholder="请输入权限值">
        </div>
    </div>
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">访问路径</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" name="uri" value="${(permission.realName)!}" placeholder="请输入访问路径">
        </div>
    </div>
	<div class="layui-form-item layui-row layui-col-xs12">
		<label class="layui-form-label">图标</label>
		<div class="layui-input-block">
			<input type="text" class="layui-input" name="icon" value="${(permission.icon)!}"  placeholder="请输入图标">
		</div>
	</div>
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">排序</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" name="orders" value="${(permission.orders)!}"  placeholder="请输入排序">
        </div>
    </div>
	<div class="layui-row">
		<div class="magb15 layui-col-md4 layui-col-xs12">
			<label class="layui-form-label">状态</label>
			<div class="layui-input-block status">
				<input type="radio" name="status" value="1" title="正常" <#if ((permission.status)!-1)==1>checked</#if> >
				<input type="radio" name="status" value="0" title="禁止" <#if ((permission.status)!-1)==2>checked</#if> >
			</div>
		</div>
	</div>
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">类型</label>
        <div class="layui-input-block">
            <select name="type">
				<option value="1">菜单</option>
				<option value="2">目录</option>
				<option value="3">按钮</option>
			</select>
        </div>
    </div>
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">上级</label>
        <div class="layui-input-block">
            <select name="pid">
                <option value="">无</option>
				<#list pList as p>
                <option value="${p.id}">${p.name}</option>
				</#list>
            </select>
        </div>
    </div>
	<#--<div class="layui-form-item layui-row layui-col-xs12">-->
		<#--<label class="layui-form-label">用户简介</label>-->
		<#--<div class="layui-input-block">-->
			<#--<textarea placeholder="请输入用户简介" class="layui-textarea userDesc"></textarea>-->
		<#--</div>-->
	<#--</div>-->
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
            $.post(basePath+"/permission/add",
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