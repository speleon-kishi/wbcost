<template>
	<div class="jumbotron">
		<form class="form-horizontal" role="form" @submit.prevent="addGoods">
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<h1>添加商品</h1>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-8">
					<label for="goodsName">商品名</label>
					<input type="text" class="form-control" id="goodsName" placeholder="请输入商品名(必填)" v-model="goods.itemName">
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-8">
					<label for="goodsDescrip">描述</label>
					<input type="text" class="form-control" id="goodsDescrip" placeholder="请输入描述(必填)" v-model="goods.itemDescription">
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-8">
					<label for="goodsPrice">价格</label>
					<input type="text" class="form-control" id="goodsPrice" placeholder="请输入价格(必填)" v-model="goods.price">
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-8">
					<label for="goodsPic">图片链接</label>
					<input type="text" class="form-control" id="goodsPic" placeholder="请输入图片链接(选填)" v-model="goods.picUrl">
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-8">
					<button type="submit" class="btn btn-success btn-lg">确定</button>
				</div>
			</div>
		</form>
	</div>
</template>

<script>
	module.exports = {
		data: function() {
			return {
				userID: '',
				goods: {
					itemName: '',
					itemDescription: '',
					price: '',
					id: '',
					status: 0,
					pic_url: ''
				}
			}
		},
		methods: {
			addGoods: function() {
				if (this.goods.itemName.trim() === "" || this.goods.itemName.trim() === null || this.goods.itemName.trim() ===
					undefined) {
					alert("请输入商品名！");
					return false;
				}
				if (this.goods.itemDescription.trim() === "" || this.goods.itemDescription.trim() === null || this.goods.itemDescription
					.trim() === undefined) {
					alert("请输入商品描述！");
					return false;
				}
				let regex = /^\d+(\.\d+)?$/;
				if (typeof this.goods.price === "string") {
					let tmpAmount = this.goods.price.trim();
					if (tmpAmount !== "" && tmpAmount !== null && tmpAmount !== undefined) {
						if (regex.test(tmpAmount)) {
							tmpAmount = parseFloat(tmpAmount);
							this.goods.price = tmpAmount;
						} else {
							alert("请输入正确的商品价格！");
							return false;
						}
					} else {
						alert("请输入商品价格！");
						return false;
					}
				} else {
					let tmpAmount = this.goods.price;
					if (tmpAmount !== null && tmpAmount !== undefined) {
						if (regex.test(tmpAmount)) {
							this.goods.price = tmpAmount;
						} else {
							alert("请输入正确的商品价格！");
							return false;
						}
					} else {
						alert("请输入商品价格！");
						return false;
					}
				}
				$.ajax({
					url: conf.ur + "/api/admin/addGoods",
					type: 'post',
					dataType: 'json',
					data: JSON.stringify(this.goods),
					headers: {
						"Content-Type": "application/json",
						"x-auth-token": window.localStorage.getItem('x-auth-token')
					},
					success: (data) => {
						if (data.code === 0) {
							alert("添加成功！");
							window.location.reload();
						}else{
							alert("添加失败！")
							console.log(JSON.stringify(data))
							window.location.reload();
						}
					},
					error: (err) => {
						console.log("网络异常！")
					}
				});
				return false;
			}
		}
	}
</script>

<style scoped>
	.jumbotron {
		background-color: ghostwhite;
	}
</style>
