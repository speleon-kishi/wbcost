<template>
	<div>
		<div class="jumbotron">
			<form class="form-horizontal" role="form" @submit.prevent="login">
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<h1>计费管理</h1>
					</div>
				</div>
				<div class="col-sm-2"></div>
				<div class="form-group table-responsive col-sm-8">
					<table class="table">
						<thead align="right">
							<tr>
								<th>身份证号</th>
								<th>真实姓名</th>
								<th>开始上机时间</th>
								<th>上机时长</th>
								<th>储值卡余额</th>
								<th>当前购物费用</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<tr v-for="item in items">
								<td>{{item.username}}</td>
								<td>{{item.trueName}}</td>
								<td>{{calcDateTime(item.lastlogin)}}</td>
								<td>{{item.lastlogin===null?calcddhhmmss(0):calcddhhmmss(currentTimeStamp-new Date(calcDateTime(item.lastlogin)).getTime())}}</td>
								<td>{{item.balance}} 元</td>
								<td>{{item.buyGoodsCost}} 元</td>
								<td>
									<button type="button" class="btn btn-success" @click="genBill(item)">结账下机</button>
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
							<h4 class="modal-title" id="editModalLabel">消费信息</h4>
						</div>
						<div class="modal-body">
							<p>网费：3元/小时（不足1小时的按1小时计费）</p>
							<p>身份证号：{{bills.username}}</p>
							<p>开始上机时间：{{calcDateTime(bills.lastlogin)}}</p>
							<p>上机时长：{{calcddhhmmss(payTime-new Date(calcDateTime(bills.lastlogin)).getTime())}}</p>
							<p>本次网费：{{this.bills.costs}} 元</p>
							<p>本次购物费：{{this.bills.buyGoodsCost}} 元</p>
							<p>储值卡原余额：{{bills.balance}} 元</p>
							<p>消费后余额：{{afterPayBalance<0?0:afterPayBalance}} 元</p>
							<p>需缴纳现金：{{afterPayBalance<0?-afterPayBalance:0}} 元</p>
							<button type="button" @click="exportBills()" class="btn btn-primary btn-lg btn-block">导出账单</button>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
							<button type="button" class="btn btn-primary" @click="payAction()">
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
			setInterval(
				() => {
					this.currentTimeStamp = new Date().getTime();
				}, 1000
			);
			setTimeout(() => {
				$.ajax({
					url: conf.ur + "/api/admin/listUsers",
					type: 'get',
					dataType: 'json',
					headers: {
						"Content-Type": "application/json",
						"x-auth-token": window.localStorage.getItem('x-auth-token')
					},
					success: (data) => {
						if (data.code === 0) {
							this.items = data.data;
							let new_date = new Date(+new Date(data.data[0].lastlogin) + 8 * 3600 * 1000).toISOString()
								.replace(/T/g, ' ')
								.replace(/\.[\d]{3}Z/, '')
						} else {
							console.log("获取失败！")
						}
					},
					error: (err) => {
						console.log("网络异常！")
					}
				});
			}, 500)
		},
		watch: {
			currentTimeStamp(val) {
				this.currentTimeStamp = val;
			}
		},
		data: function() {
			return {
				userID: '',
				items: [],
				currentTimeStamp: 0,
				bills: {
					username: '',
					lastlogin: '2020-03-19T16:06:04.000+0000',
					balance: 0,
					costs: 0,
					buyGoodsCost: 0
				},
				payTime: new Date().getTime(),
				afterPayBalance: 0,
			}
		},
		methods: {
			calcDateTime(datetime) {
				return new Date(+new Date(datetime) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
			},
			calcddhhmmss(mss) {
				let days = parseInt(mss / (1000 * 60 * 60 * 24));
				let hours = parseInt((mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
				let minutes = parseInt((mss % (1000 * 60 * 60)) / (1000 * 60));
				let seconds = (mss % (1000 * 60)) / 1000;
				return days + " 天 " + hours + " 小时 " + minutes + " 分钟 " + parseInt(seconds) + " 秒 ";
			},
			genBill(item) {
				$('#editModal').modal();
				this.bills = JSON.parse(JSON.stringify(item));
				this.payTime = new Date().getTime();
				let mss = this.payTime - new Date(this.calcDateTime(this.bills.lastlogin)).getTime();
				let days = parseInt(mss / (1000 * 60 * 60 * 24));
				let hours = Math.ceil((mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
				this.afterPayBalance = this.bills.balance - hours * 3 - days * 24 * 3 - this.bills.buyGoodsCost;
				this.bills.costs = Math.abs(0 - hours * 3 - days * 24 * 3);
			},
			payAction() {
				this.bills.afterPayBalance = this.afterPayBalance;
				$.ajax({
					url: conf.ur + "/api/admin/payAction",
					type: 'post',
					dataType: 'json',
					data: JSON.stringify(this.bills),
					headers: {
						"Content-Type": "application/json",
						"x-auth-token": window.localStorage.getItem('x-auth-token')
					},
					success: (data) => {
						if (data.code === 0) {
							alert("下机成功！");
							window.location.reload();
						} else {
							alert("下机成功！")
							window.location.reload();
						}
					},
					error: (err) => {
						console.log("网络异常！")
					}
				});
			},
			exportBills() {
				this.bills.afterPayBalance = this.afterPayBalance;
				let toPost = {
					baseInfo: [],
					foodInfo: []
				};
				toPost.baseInfo = [
					"网费：3元/小时（不足1小时的按1小时计费）",
					"身份证号：" + this.bills.username,
					"开始上机时间：" + this.calcDateTime(this.bills.lastlogin),
					"上机时长：" + this.calcddhhmmss(this.payTime - new Date(this.calcDateTime(this.bills.lastlogin)).getTime()),
					"储值卡原余额：" + this.bills.balance + " 元"
				]
				if (this.bills.afterPayBalance < 0) {
					toPost.baseInfo.push("消费后余额：0 元")
					toPost.baseInfo.push("需缴纳现金：" + (-this.bills.afterPayBalance) + " 元")
				} else {
					toPost.baseInfo.push("消费后余额：" + this.bills.afterPayBalance + " 元")
					toPost.baseInfo.push("需缴纳现金：0 元")
				}
				let url = conf.ur + "/api/public/exportBills";
				let bsInfo = JSON.stringify(toPost.baseInfo);
				let fdInfo = JSON.stringify(toPost.foodInfo);
				bsInfo = bsInfo.replace("[", "");
				bsInfo = bsInfo.replace("]", "");
				fdInfo = fdInfo.replace("[", "");
				fdInfo = fdInfo.replace("]", "");
				let exportFile = () => {
					var form = $("<form></form>");
					form.attr("style", "display:none");
					form.attr("target", "");
					form.attr("method", "post");
					form.attr("action", url);
					let baseInfo = $("<input type='text' name='baseInfo'/>");
					baseInfo.attr("value", bsInfo);
					form.append(baseInfo);
					let foodInfo = $("<input type='text' name='foodInfo'/>");
					foodInfo.attr("value", fdInfo);
					form.append(foodInfo);
					$("body").append(form);
					form.submit();
				}
				exportFile();
			}
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
