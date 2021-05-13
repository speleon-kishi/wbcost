<template>
	<div class="jumbotron">
		<form class="form-horizontal" role="form" @submit.prevent="addPresent">
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<h1>添加礼品</h1>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-8">
					<label for="presentName">礼品名</label>
					<input type="text" class="form-control" id="presentName" placeholder="请输入礼品名(必填)" v-model="present.presentName">
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-8">
					<label for="presentDescrip">描述</label>
					<input type="text" class="form-control" id="presentDescrip" placeholder="请输入描述(必填)" v-model="present.presentDescrip">
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-8">
					<label for="presentCosts">所需积分</label>
					<input type="text" class="form-control" id="presentCosts" placeholder="请输入礼品兑换所需积分(必填)" v-model="present.presentCosts">
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-8">
					<label for="picUrl">图片链接</label>
					<input type="text" class="form-control" id="picUrl" placeholder="请输入图片链接(选填)" v-model="present.picUrl">
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
				present: {
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
			addPresent: function() {
				if (this.present.presentName.trim() === "" || this.present.presentName.trim() === null || this.present.presentName.trim() ===
					undefined) {
					alert("请输入礼品名！");
					return false;
				}
				if (this.present.presentDescrip.trim() === "" || this.present.presentDescrip.trim() === null || this.present.presentDescrip
					.trim() === undefined) {
					alert("请输入礼品描述！");
					return false;
				}
				let regex = /^\d+(\.\d+)?$/;
				if (typeof this.present.presentCosts === "string") {
					let tmpAmount = this.present.presentCosts.trim();
					if (tmpAmount !== "" && tmpAmount !== null && tmpAmount !== undefined) {
						if (regex.test(tmpAmount)) {
							tmpAmount = parseFloat(tmpAmount);
							this.present.presentCosts = tmpAmount;
						} else {
							alert("请输入正确的礼品兑换所需积分！");
							return false;
						}
					} else {
						alert("请输入礼品兑换所需积分！");
						return false;
					}
				} else {
					let tmpAmount = this.present.presentCosts;
					if (tmpAmount !== null && tmpAmount !== undefined) {
						if (regex.test(tmpAmount)) {
							this.present.presentCosts = tmpAmount;
						} else {
							alert("请输入正确的礼品兑换所需积分！");
							return false;
						}
					} else {
						alert("请输入礼品兑换所需积分！");
						return false;
					}
				}
				if (this.present.picUrl.trim() === "" || this.present.picUrl.trim() === null || this.present.picUrl.trim() ===
					undefined) {
					this.present.picUrl = null;
				}
				$.ajax({
					url: conf.ur + "/api/admin/addPresent",
					type: 'post',
					dataType: 'json',
					data: JSON.stringify(this.present),
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
