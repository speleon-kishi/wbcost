<template>
	<div class="jumbotron">
		<h1>{{hintText}}</h1>
		<p>欢迎回来，{{customerUser.trueName}}</p>
		<p>本账号状态：{{this.online==="0"?"离线":"在线"}}</p>
		<p>储值卡余额：{{this.balance}} 元</p>
		<p>当前积分：{{this.point}} 个</p>
	</div>
</template>

<script>
	module.exports = {
		data: function() {
			return {
				hintText: '欢迎登陆网吧计费管理系统！',
				customerUser: {},
				online: 0,
				balance: 0,
				point: 0
			}
		},
		mounted() {
			this.customerUser = JSON.parse(localStorage.getItem('customerUser'));
			$.ajax({
				url: conf.ur + "/api/public/selectIsOnline?id="+this.customerUser.id,
				type: 'get',
				headers: {
					"Content-Type": "application/json"
				},
				success: (data) => {
					this.online = data.data.online;
					this.balance = data.data.balance;
					this.point = data.data.point;
				},
				error: (err) => {
					console.log("网络异常！")
				}
			});
		},
	}
</script>