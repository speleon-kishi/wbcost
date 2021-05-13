<template>
	<div class="jumbotron">
		<form class="form-horizontal" role="form" @submit.prevent="login">
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<h1>登记上机</h1>
				</div>
			</div>
			<div class="form-group">
				<label for="userID" class="col-sm-2 control-label">身份证号</label>
				<div class="col-sm-10">
					<input type="text" class="form-control" v-model="userID" v-focus="true" placeholder="请输入身份证号(用户名)">
				</div>
			</div>
			<div class="form-group">
				<label for="trueName" class="col-sm-2 control-label">真实姓名</label>
				<div class="col-sm-10">
					<input type="text" class="form-control" v-model="trueName" placeholder="请输入真实姓名">
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<div class="checkbox">
						<label class="radio-inline">
							<input type="radio" name="whenPay" id="optionsRadios3" value="laterPay" checked @change="changePay">后付费
						</label>
						<label class="radio-inline">
							<input type="radio" name="whenPay" id="optionsRadios4" value="alreadyPay" @change="changePay">预付费
						</label>
					</div>
				</div>
			</div>
			<div class="form-group" v-show="isAlreadyPay">
				<label for="payAmount" class="col-sm-2 control-label">预付费金额</label>
				<div class="col-sm-10">
					<input type="text" class="form-control" v-model="payAmount" placeholder="请输入预付金额">
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-success btn-lg">登录</button>
				</div>
			</div>
		</form>
	</div>
</template>

<script>
	module.exports = {
		directives: {
			focus: {
				inserted: function(el, {
					value
				}) {
					if (value) {
						el.focus();
					}
				}
			}
		},
		mounted() {

		},
		data: function() {
			return {
				hintText: '登记上机！',
				userID: '',
				trueName: '',
				payAmount: '',
				isAlreadyPay: false,
			}
		},
		methods: {
			login: function() {
				if (this.trueName.trim() === "" || this.trueName.trim() === null || this.trueName.trim() === undefined) {
					alert("请输入用户姓名！");
					return false;
				}
				if (this.userID.trim() === "" || this.userID.trim() === null || this.userID.trim() === undefined) {
					alert("请输入身份证号！");
					return false;
				}
				if(checkID(this.userID)===false){
					alert("请输入正确的身份证号！");
					return false;
				}
				let tmpAmount = this.payAmount.trim();
				if (typeof tmpAmount === "string") {
					let regex = /^\d+(\.\d+)?$/;
					if (tmpAmount !== "" && tmpAmount !== null && tmpAmount !== undefined) {
						if (regex.test(tmpAmount)) {
							tmpAmount = parseFloat(tmpAmount);
						} else {
							alert("请输入正确的金额！");
							return false;
						}
					}
				} else {
					alert("请输入正确的金额！");
					return false;
				}
				$.ajax({
					url: conf.ur + "/api/admin/registerUser",
					type: 'post',
					dataType: 'json',
					data: JSON.stringify({
						"idNum": this.userID.trim(),
						"payAmount": tmpAmount,
						"trueName": this.trueName
					}),
					headers: {
						"Content-Type": "application/json",
						"x-auth-token": window.localStorage.getItem('x-auth-token')
					},
					success: (data) => {
						if (data.code === 0) {
							alert("上机成功！");
							window.location.reload();
						}
					},
					error: (err) => {
						console.log("网络异常！")
					}
				});
				return false;
			},
			changePay: function() {
				this.isAlreadyPay = true;
			}
		}
	}
</script>

<style scoped>
	#hint-div {
		width: 700px;
		height: 200px;
		position: absolute;
		left: 40%;
		top: 40%;
		margin: -50px 0 0 -50px;
		text-align: center;
	}

	.jumbotron {
		background-color: ghostwhite;
	}
</style>
