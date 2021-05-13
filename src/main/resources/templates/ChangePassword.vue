<template>
	<div>
		<div class="jumbotron">
			<form class="form-horizontal" role="form" @submit.prevent="login">
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<h1>用户管理</h1>
					</div>
				</div>
				<div class="col-sm-2"></div>
				<div class="form-group table-responsive col-sm-8">
					<table class="table">
						<thead align="right">
							<tr>
								<th>用户名</th>
								<th>真实姓名</th>
								<th>储值卡余额</th>
								<th>在线状态</th>
								<th>可用积分</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<tr v-for="item in items">
								<td>{{item.username}}</td>
								<td>{{item.trueName}}</td>
								<td>{{item.balance}} 元</td>
								<td v-if="item.online===0">
									<button type="button" class="btn btn-default">离线</button>
								</td>
								<td v-else>
									<button type="button" class="btn btn-success">在线</button>
								</td>
								<td>{{item.userPoint}} 个</td>
								<td>
								    <button type="button" class="btn btn-warning" @click="changePwd(item)">修改密码</button>
								    <button type="button" class="btn btn-warning" @click="del(item)">刪除用戶</button>
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
							<h4 class="modal-title" id="editModalLabel">修改密码</h4>
						</div>
						<div class="modal-body">
							<div class="form-group">
								<label for="customerPwd">新密码</label>
								<input type="text" class="form-control" id="customerPwd" placeholder="请输入新密码" v-model="customer.password">
							</div>
							<div class="form-group">
								<label for="customerPwdRe">确认新密码</label>
								<input type="text" class="form-control" id="customerPwdRe" placeholder="请确认新密码" v-model="customer.rePassword">
							</div>
							<input type="hidden" v-model="customer.id" />
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">关闭
							</button>
							<button type="button" class="btn btn-primary" @click="doChangePwd(customer)">
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
            this.lods();
		},
		data: function() {
			return {
				userID: '',
				items: [],
				customer: {
					id: 0,
					password: '',
					rePassword: ''
				}
			}
		},
		methods: {
		    lods(){
                $.ajax({
                    url: conf.ur + "/api/admin/getAllUsers",
                    type: 'get',
                    dataType: 'json',
                    headers: {
                        "Content-Type": "application/json",
                        "x-auth-token": window.localStorage.getItem('x-auth-token')
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
			changePwd(item) {
				$('#editModal').modal();
				this.customer = JSON.parse(JSON.stringify(item));
				this.customer.password = '';
			},
			doChangePwd(item) {
				let customers = JSON.parse(JSON.stringify(item));
				if (customers.password.trim() === "" || customers.password.trim() === null || customers.password.trim() ===
					undefined) {
					alert("密码不能为空！");
					return false;
				}
				if(customers.password.trim()!==customers.rePassword.trim()){
					alert("两次密码输入不一致！");
					return false;
				}
				$.ajax({
					url: conf.ur + "/api/admin/doChangePwd",
					type: 'post',
					dataType: 'json',
					data: JSON.stringify(customers),
					headers: {
						"Content-Type": "application/json",
						"x-auth-token": window.localStorage.getItem('x-auth-token')
					},
					success: (data) => {
						if (data.code === 0) {
							alert("修改成功！");
							window.location.reload();
						}else{
							alert("修改失败！！")
							window.location.reload();
						}
					},
					error: (err) => {
						console.log("网络异常！")
					}
				});
			},
			del(item){
			    if(confirm('确定删除！')==true){
                     $.ajax({
                        url: conf.ur + "/api/admin/del?id="+item.id,
                        type: 'post',
                        dataType: 'json',
                        headers: {
                            "Content-Type": "application/json",
                            "x-auth-token": window.localStorage.getItem('x-auth-token')
                        },
                        success: (data) => {
                            if (data.code === 0) {
                                alert("刪除成功！");
                                this.lods();
                            }else{
                                alert("刪除失败！！")
                                this.lods();
                            }
                        },
                        error: (err) => {
                            console.log("网络异常！")
                        }
                    });
                }
			}
		}
	}
</script>

<style scoped>
	.jumbotron {
		background-color: ghostwhite;
	}
</style>
