<template>
	<div class="jumbotron">
		<h1>{{hintText}}</h1>
		<p>欢迎回来，{{sysUser.username}}</p>
		<p>当前上机人数：{{online}}人</p>
	</div>
</template>

<script>
	module.exports = {
		data: function() {
			return {
				hintText: '欢迎登陆网吧计费管理系统！',
				sysUser: {},
				online: 0
			}
		},
		mounted() {
			this.sysUser = JSON.parse(localStorage.getItem('sysUser'));
			$.ajax({
				url: conf.ur + "/api/public/listAllOnline",
				type: 'get',
				headers: {
					"Content-Type": "application/json"
				},
				success: (data) => {
					this.online = data.data;
				},
				error: (err) => {
					console.log("网络异常！")
				}
			});
		},
	}
</script>