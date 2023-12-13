<template>

  <div class="log">

 <v-row  >
  <v-col >

  <v-card
    class="mx-auto"
    max-width="660"
    title="User Registration"
  >

  <div class="logleft">
          <v-img class="divleft"
      height="425"
      width="230"
      src="../../public/下载.svg"
    ></v-img>
    <img src="../../public/logo.png" class="ccc">
    <p class="abc">Mark++</p>
    <!-- <p class="bbb">Explore the unkown<br> world togrther</p> -->
 </div>
  
    <v-container >
      <br>
      <v-row>
        <v-col >
      <span class="logheadline">注册</span>
      </v-col>
      </v-row>
 
      <br>
      <br>
    <v-row  >
     <v-col offset="1">
      <v-text-field
        v-model="name"
        color="red"
        label="姓名"
        variant="underlined"
      ></v-text-field>

      <v-text-field
        v-model="email"
        color="primary"
        label="Email"
        variant="underlined"
      ></v-text-field>

      <v-text-field
        v-model="password"
        color="primary"
        label="密码"
        placeholder="Enter your password"
        variant="underlined"
      ></v-text-field>

      <v-checkbox
        v-model="terms"
        color="secondary"
        label="I agree to site terms and conditions"
      ></v-checkbox>
     </v-col>
     </v-row>
    </v-container>

    <v-divider></v-divider>

    <v-card-actions>
      <v-spacer></v-spacer>

      <v-btn color="grey" @click="handleRegistration">
  完成注册
  <v-icon icon="mdi-chevron-right" end></v-icon>
</v-btn>

    </v-card-actions>
  </v-card>
   </v-col>
  </v-row>
  </div>

</template>

<script>
// 在你的脚本部分（script）中引入 Vue Router

  export default {
    data: () => ({
      first: null,
      last: null,
      email: null,
      password: null,
      terms: false,
    }),
    methods: {
    // 添加一个处理注册按钮点击的方法
    async handleRegistration() {
      if (this.name && this.email && this.password && this.terms) {
        // 表单有效，可以注册
        // 这里可以加入其他注册逻辑，例如发送注册请求到服务器等

        // 注册成功后，使用路由对象跳转到主界面
        this.$router.push('/home');
      } else {
        // 表单无效，给予用户反馈，例如显示错误消息
        alert('请填写完整的注册信息');
      }
    },
    },
  }
</script>

<style >
.logleft{
  flex: 1; /* 填满剩余空间 */
  position: relative;
}
  .log {
    display: flex;
    justify-content: center;
    align-items: center;
    background-image: url('../../public/logbgp.jpg');
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    height: 100vh;
  }
  .abc{
  position: absolute;
  left:  68px;
  top:16px;
  color:AntiqueWhite;
    font-family: "Courier New";
  font-size: 30px;
}
.logheadline{
  position: absolute;
  left:  265px;
  top:20px;
  font-family: "Courier New";
  color:rgb(23, 21, 21);
  font-size: 30px;
}
</style>

