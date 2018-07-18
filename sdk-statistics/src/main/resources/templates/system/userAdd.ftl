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
		<label class="layui-form-label">用户名</label>
		<div class="layui-input-block">
			<input type="text" class="layui-input username" name="username" value="${(user.username)!}" lay-verify="required" placeholder="请输入用户名">
		</div>
	</div>
	<#if !(user??)>
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">密码</label>
        <div class="layui-input-block">
            <input type="password" class="layui-input password" name="password" lay-verify="required" placeholder="请输入密码">
        </div>
    </div>
	</#if>
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">真实姓名</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input realName" name="realName" value="${(user.realName)!}" placeholder="请输入真实姓名">
        </div>
    </div>
	<div class="layui-form-item layui-row layui-col-xs12">
		<label class="layui-form-label">手机号码</label>
		<div class="layui-input-block">
			<input type="text" class="layui-input phone" name="phone" value="${(user.phone)!}"  placeholder="请输入手机号码">
		</div>
	</div>
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">邮箱</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input email" name="email" value="${(user.email)!}"  placeholder="请输入邮箱">
        </div>
    </div>
	<div class="layui-row">
		<div class="magb15 layui-col-md4 layui-col-xs12">
			<label class="layui-form-label">性别</label>
			<div class="layui-input-block sex">
				<input type="radio" name="sex" value="1" title="男" <#if ((user.sex)!-1)==1>checked</#if> >
				<input type="radio" name="sex" value="2" title="女" <#if ((user.sex)!-1)==2>checked</#if> >
				<input type="radio" name="sex" value="0" title="保密" <#if ((user.sex)!-1)==0>checked</#if>>
			</div>
		</div>
            <#--<div class="magb15 layui-col-md4 layui-col-xs12">-->
                <#--<label class="layui-form-label">状态</label>-->
                <#--<div class="layui-input-block locked">-->
                    <#--<input type="radio" name="locked" value="1" title="锁定" checked>-->
                    <#--<input type="radio" name="locked" value="0" title="正常">-->
                <#--</div>-->
            <#--</div>-->
		<div class="magb15 layui-col-md4 layui-col-xs12">
			<label class="layui-form-label">用户状态</label>
			<div class="layui-input-block">
				<select name="locked" class="locked" lay-filter="status">
					<option value="0" <#if ((user.locked)!-1)==0>selected</#if>>正常使用</option>
					<option value="1" <#if ((user.locked)!-1)==1>selected</#if>>锁定用户</option>
				</select>
			</div>
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
<script type="text/javascript" src="${basePath}/js/userAdd.js"></script>
</body>
</html>