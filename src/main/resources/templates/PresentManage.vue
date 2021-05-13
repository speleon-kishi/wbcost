<template>
	<div>
		<div class="jumbotron">
			<form class="form-horizontal" role="form" @submit.prevent="login">
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<h1>礼品管理</h1>
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
									<button type="button" class="btn btn-primary" @click="editItem(item)">修改</button>
									<button type="button" class="btn btn-warning" @click="withdrawItem(item)">下架</button>
								</td>
								<td v-else>
									<button type="button" class="btn btn-success" @click="republishItem(item)">重新上架</button>
									<button type="button" class="btn btn-danger" @click="deleteItem(item)">删除</button>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</form>

			<div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="editModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
							<h4 class="modal-title" id="editModalLabel">修改礼品信息</h4>
						</div>
						<div class="modal-body">
							<div class="form-group">
								<label for="presentName">商品名</label>
								<input type="text" class="form-control" id="presentName" placeholder="请输入商品名" v-model="goods.presentName">
							</div>
							<div class="form-group">
								<label for="presentDescrip">描述</label>
								<input type="text" class="form-control" id="presentDescrip" placeholder="请输入描述" v-model="goods.presentDescrip">
							</div>
							<div class="form-group">
								<label for="presentCosts">价格</label>
								<input type="text" class="form-control" id="presentCosts" placeholder="请输入价格" v-model="goods.presentCosts">
							</div>
							<div class="form-group">
								<label for="goodsPic">图片链接</label>
								<input type="text" class="form-control" id="goodsPic" placeholder="请输入图片链接" v-model="goods.picUrl">
							</div>
							<input type="hidden" v-model="goods.id" />
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">关闭
							</button>
							<button type="button" class="btn btn-primary" @click="doEdit(goods)">
								提交
							</button>
						</div>
					</div>
				</div>
			</div>
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
				userID: '',
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
			editItem(item) {
				$('#editModal').modal();
				this.goods = JSON.parse(JSON.stringify(item));
			},
			doEdit(item) {
				let regex = /^\d+(\.\d+)?$/;
				if (typeof this.goods.presentCosts === "string") {
					let tmpAmount = this.goods.presentCosts.trim();
					if (tmpAmount !== "" && tmpAmount !== null && tmpAmount !== undefined) {
						if (regex.test(tmpAmount)) {
							tmpAmount = parseFloat(tmpAmount);
							this.goods.presentCosts = tmpAmount;
						} else {
							alert("请输入正确的礼品兑换所需积分！");
							return false;
						}
					} else {
						alert("请输入礼品兑换所需积分！");
						return false;
					}
				} else {
					let tmpAmount = this.goods.presentCosts;
					if (tmpAmount !== null && tmpAmount !== undefined) {
						if (regex.test(tmpAmount)) {
							this.goods.presentCosts = tmpAmount;
						} else {
							alert("请输入正确的礼品兑换所需积分！");
							return false;
						}
					} else {
						alert("请输入礼品兑换所需积分！");
						return false;
					}
				}
				$.ajax({
					url: conf.ur + "/api/admin/editPresent",
					type: 'post',
					dataType: 'json',
					data: JSON.stringify(this.goods),
					headers: {
						"Content-Type": "application/json",
						"x-auth-token": window.localStorage.getItem('x-auth-token')
					},
					success: (data) => {
						if (data.code === 0) {
							alert("修改成功！");
							window.location.reload();
						}else{
							alert("修改失败！")
							window.location.reload();
						}
					},
					error: (err) => {
						console.log("网络异常！")
					}
				});
			},
			withdrawItem(item) {
				if (confirm("确认下架该商品吗？") == true) {
					let goods = JSON.parse(JSON.stringify(item));
					goods.status = 1;
					$.ajax({
						url: conf.ur + "/api/admin/changePresentStatus",
						type: 'post',
						dataType: 'json',
						data: JSON.stringify(goods),
						headers: {
							"Content-Type": "application/json",
							"x-auth-token": window.localStorage.getItem('x-auth-token')
						},
						success: (data) => {
							if (data.code === 0) {
								alert("下架成功！");
								window.location.reload();
							}else{
								alert("下架失败！")
								window.location.reload();
							}
						},
						error: (err) => {
							console.log("网络异常！")
						}
					});
				}
			},
			deleteItem(item) {
				if (confirm("确认删除该商品吗？") == true) {
					let goods = JSON.parse(JSON.stringify(item));
					$.ajax({
						url: conf.ur + "/api/admin/deletePresent",
						type: 'post',
						dataType: 'json',
						data: JSON.stringify(goods),
						headers: {
							"Content-Type": "application/json",
							"x-auth-token": window.localStorage.getItem('x-auth-token')
						},
						success: (data) => {
							if (data.code === 0) {
								alert("删除成功！");
								window.location.reload();
							}else{
								alert("删除失败！")
								window.location.reload();
							}
						},
						error: (err) => {
							console.log("网络异常！")
						}
					});
				}
			},
			republishItem(item) {
				if (confirm("确认重新上架该商品吗？") == true) {
					let goods = JSON.parse(JSON.stringify(item));
					goods.status = 0;
					$.ajax({
						url: conf.ur + "/api/admin/changePresentStatus",
						type: 'post',
						dataType: 'json',
						data: JSON.stringify(goods),
						headers: {
							"Content-Type": "application/json",
							"x-auth-token": window.localStorage.getItem('x-auth-token')
						},
						success: (data) => {
							if (data.code === 0) {
								alert("重新上架成功！");
								window.location.reload();
							}else{
								alert("重新上架失败！")
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
