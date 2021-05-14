<template>
	<div>
		<div class="jumbotron">
			<form class="form-horizontal" role="form" @submit.prevent="">
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<h1>购买商品</h1>
					</div>
				</div>
				<div class="col-sm-2"></div>
				<div class="form-group table-responsive col-sm-8">
					<table class="table">
						<thead align="right">
							<tr>
								<th>品名</th>
								<th>图片</th>
								<th>价格</th>
								<th>数量</th>
								<th>金额</th>
								<th>描述</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<tr v-for="(item,index) in items">
								<td>{{item.itemName}}</td>
								<td><img class="goodsImg" :src="item.picUrl"></td>

								<td>{{item.price}}</td><td>
									<button @click="decrement(index)" :disabled="item.num <=0">-</button>
								<input  v-model="item.num" v-on:change="changeTotalPrice(index,$event)" style="width: 30px;text-align: center">
									<button @click="increment(index)">+</button>
								</td>
								<td>
									{{item.totalprice}}
								</td>
								<td>{{item.itemDescription}}</td>
								<td v-if="item.status===0">
									<button type="button" class="btn btn-success" @click="buyItem(item)">购买</button>
								</td>
								<td v-else>
									<button type="button" class="btn btn-default" disabled="disabled">购买</button>
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
				url: conf.ur + "/api/public/listGoods",
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
					itemName: '',
					itemDescription: '',
					price: '',
					num:'',
					totalprice:'',
					id: '',
					status: 0,
					pic_url: ''
				}
			}
		},
		methods: {
			increment(index){
				this.items[index].num++
				this.items[index].totalprice=this.items[index].num*this.items[index].price
			},
			decrement(index){
				this.items[index].num--;
				this.items[index].totalprice=this.items[index].num*this.items[index].price

			},
			changeTotalPrice(index,e){
				var a=e.target.value;
                				if (e.target.value==''){
                					this.items[index].num=0;
                					e.target.value=0;
                					this.items[index].totalprice=this.items[index].num*this.items[index].price
                					return
                				}
                				e.target.value=a;
                				var re=/^[0-9]+[0-9]*]*$/ ;
                				if (!re.test(e.target.value)){
                					alert("请输入自然数")
                					this.items[index].num=e.target.value.replace(/[^\d]/g,'');
                					if (this.items[index].num.length>=2) {
                						this.items[index].num=this.items[index].num.replace(/^0+/g,"");
                					}
                					this.items[index].totalprice=this.items[index].num*this.items[index].price
                					return;
                				}
                				if (e.target.value.length>=2) {
                					a=e.target.value.replace(/^0+/g,"");
                				}
                				this.items[index].num=a;
                				this.items[index].totalprice=this.items[index].num*this.items[index].price

			},
			buyItem(item) {
				if (confirm("确认购买该商品吗？") == true) {
					let goods = JSON.parse(JSON.stringify(item));
					this.customerUser = JSON.parse(localStorage.getItem('customerUser'));
					goods.uid = this.customerUser.id;
					// let totalCosts = 0;
					// (localStorage.getItem("totalCosts") === null) ? totalCosts = 0: totalCosts = parseFloat(localStorage.getItem(
					// 	"totalCosts"));
					$.ajax({
						url: conf.ur + "/api/public/buyGoods",
						type: 'post',
						dataType: 'json',
						data: JSON.stringify(goods),
						headers: {
							"Content-Type": "application/json",
							"x-auth-token": window.localStorage.getItem('x-auth-token')
						},
						success: (data) => {
							if (data.code === 0) {
								// localStorage.setItem("totalCosts", totalCosts + goods.price);
								alert("购买成功！");
								// window.location.reload();
								item.num=0;
								item.totalprice=0;
							} else {
								alert("购买失败！")
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
