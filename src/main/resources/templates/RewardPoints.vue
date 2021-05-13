<template>
	<div>
		<div class="jumbotron">
			<form class="form-horizontal" role="form" @submit.prevent="login">
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<h1>积分兑换</h1>
					</div>
				</div>
				<div class="col-sm-2"></div>
				<div class="form-group table-responsive col-sm-8">
					<table class="table">
						<thead align="right">
							<tr>
								<th>品名</th>
								<th>图片</th>
								<th>所需积分</th>
								<th>描述</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<tr v-for="item in items">
								<td>{{item.presentName}}</td>
								<td><img class="goodsImg" :src="item.picUrl"></td>
								<td>{{item.presentCosts}}</td>
								<td>{{item.presentDescrip}}</td>
								<td v-if="item.status===0">
									<button type="button" class="btn btn-success" @click="rewardItem(item)">兑换</button>
								</td>
								<td v-else>
									<button type="button" class="btn btn-default" disabled="disabled">兑换</button>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</form>
		</div>
	</div>
</template>

<script>
	module.exports = {
		mounted() {
			$.ajax({
				url: conf.ur + "/api/public/listPresent",
				type: 'get',
				dataType: 'json',
				headers: {
					"Content-Type": "application/json",
				},
				success: (data) => {
					if (data.code === 0) {
						this.items = data.data;
					} else {
						console.log("获取失败！")
					}
				},
				error: (err) => {
					console.log("网络异常！")
				}
			});
		},
		data: function() {
			return {
				customerUser: '',
				items: [],
				goods: {
					presentName: '',
					presentDescrip: '',
					presentCosts: '',
					id: '',
					status: 0,
					picUrl: ''
				}
			}
		},
		methods: {
			rewardItem(item) {
				if (confirm("确认兑换该礼品吗？") == true) {
					let goods = JSON.parse(JSON.stringify(item));
					this.customerUser = JSON.parse(localStorage.getItem('customerUser'));
					goods.uid = this.customerUser.id;
					$.ajax({
						url: conf.ur + "/api/public/rewardItem",
						type: 'post',
						dataType: 'json',
						data: JSON.stringify(goods),
						headers: {
							"Content-Type": "application/json",
							"x-auth-token": window.localStorage.getItem('user-x-auth-token')
						},
						success: (data) => {
							if (data.code === 0) {
								alert(data.data);
								window.location.reload();
							} else {
								alert(data.msg);
								window.location.reload();
							}
						},
						error: (err) => {
							console.log("网络异常！")
						}
					});
				}
			},
		}
	}
</script>

<style scoped>
	.goodsImg {
		height: 100px;
	}

	.jumbotron {
		background-color: ghostwhite;
	}
</style>
