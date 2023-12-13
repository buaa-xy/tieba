<template>
  <div class="login">
    <comment-card></comment-card>
    <!-- <v-alert
      border="left"
      color="green"
      prominent
      type="success"
      dismissible
      text
      v-show="showalert1"  
      >注册成功,请返回登录</v-alert
    >

    <v-alert
      border="left"
      color="red"
      dense
      elevation="2"
      prominent
      text
      dismissible
      type="error"
      v-show="showalert2"
      >注册失败，{{ message }}</v-alert
    > -->
    <v-container class="loginOverlay">
      <v-layout align-center justify-center>
        <v-flex xs12 sm8>
          <v-card width="600" outlined>
            <!-- <router-link to="/">
              <v-btn class="icon" color="blue lighten-5" dark max-width="150px">
                <v-icon dark left color="blue darken-3">返回</v-icon>
              </v-btn>
              <br>
              
            </router-link> -->
            <v-card-title>
              <br />
              <span class="head">Mark++ Register</span>
            </v-card-title>
            <v-spacer></v-spacer>
            <v-card-text class="pt-4">
              <v-form ref="form">
                <!-- <v-text-field
                  label="邮箱"
                  v-model="email"
                  required
                  :rules="emailRules"
                  append-icon="fa-info-circle"
                ></v-text-field> -->
                <v-text-field
                  label="用户名（不超过10位数字字母）"
                  v-model="username"
                  required
                  :rules="usernameRules"
                  append-icon="fa-info-circle"
                ></v-text-field>
                <v-text-field
                  label="密码"
                  v-model="password"
                  :rules="passwordRules"
                  :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
                  :type="show1 ? 'text' : 'password'"
                  @click:append="showpassword"
                  required
                ></v-text-field>
                                <v-text-field
                  label="email"
                  v-model="email"
                  required
                  append-icon="fa-info-circle"
                ></v-text-field>
              </v-form>
            </v-card-text>
            <v-card-actions>
              <v-btn @click="register()" class="black white--text">注册</v-btn>
            </v-card-actions>
            <br />
            <router-link to="/">
              <v-btn class="icon" color="blue lighten-5" dark max-width="150px">
                <v-icon dark left color="black">返回</v-icon>
              </v-btn>
              <br />
            </router-link>
          </v-card>
        </v-flex>
      </v-layout>
    </v-container>
  </div>
</template>

<script>
export default {
  data: () => ({
    num: 0,
    timer: null,
    timer2: null,
    alert: false,
    vlaue: 0,
    show1: false,
    showalert1: false,
    showalert2: false,
    message: "",
    username: "",
    password: "",
    email:"",
    passwordRules: [
      (v) => !!v || "密码未填写",
      (v) => v.length >= 6 || "密码必须为6~32位",
      (v) => v.length <= 32 || "密码必须为6~32位",
      // (v) => {
      //   const pattern = /^(?![^a-zA-Z]+$)(?!\D+$)/;
      //   return pattern.test(v) || "密码必须同时包含数字与字母";
      // },
    ],
    usernameRules: [
      (v) => !!v || "用户名未填写",
      (v) => v.length <= 10 || "用户名长度不超过10",
      // (v) => {
      //   const pattern = /^[0-9A-Za-z]+$/;
      //   return pattern.test(v) || "用户名只能包含数字字母";
      // },
    ],
  }),

  methods: {
    clear() {
      clearInterval(this.timer);
      this.timer = null;
      clearInterval(this.timer2);
      this.timer2 = null;
    },
    setalert1() {
      this.showalert1 = true;
      if (this.timer == null) {
        this.timer = setInterval(() => {
          this.showalert1 = !this.showalert1;
        }, 2000);
        this.timer2 = setInterval(() => {
          this.clear();
        }, 2000);
      }
    },
    setalert2() {
      this.showalert2 = true;
      if (this.timer == null) {
        this.timer = setInterval(() => {
          this.showalert2 = !this.showalert2;
        }, 2000);
        this.timer2 = setInterval(() => {
          this.clear();
        }, 2000);
      }
    },
    showpassword() {
      this.show1 = !this.show1;
    },
    get() {
      this.value += 1;
    },
    isShow(time) {
      if (this.value > time) {
        this.alert = false;
        clearInterval(this.timer);
        this.value = 0;
      }
    },
    register() {
      if (
        this.username === "" ||
        this.password === "" ||
        this.username.length > 10 ||
        this.password.length < 6 ||
        this.password.length > 32
      ) {
        this.$notify({
          title: "注册失败",
          message: "注册失败，请检查格式是否正确",
          type: "success",
        });
        this.showalert2 = false;
      } else {
        console.log(this.username);
        console.log(this.password);
        this.$axios({
          method: "get",
          url: "/api/user/register",
          params: {
            username: this.username,
            password: this.password,
            email:this.email,
          },
        }).then((response) => {
          console.log(response.data);
          if (response.data.state === 200) {
            // localStorage.setItem("userid", response.data.data.uid);
            this.setalert1();
            console.log(response.data.data.uid);
            // this.$router.push("/");
            this.$notify({
              title: "成功",
              message: "注册成功，请前往登录",
              type: "success",
            });
            this.$router.push("/");
          } else {
            this.$notify({
              title: "失败",
              message: "注册失败，用户名重复",
              type: "success",
            });
            this.message = response.data.message;
            console.log("aaa");
            this.setalert2();
          }
        });
      }
    },
  },
};
</script>

<style scoped>
.login {
  height: 100vh;
  /* background-color: #ffffff; */
  background-image: url("../assets/bgp.png");
  background-attachment: fixed;
  background-repeat: no-repeat;
}
h4 {
  font-size: 1em !important;
  padding: 1em;
  color: black;
}
.v-card {
  text-align: center;
  margin: 1em;
}
.v-card__title {
  justify-content: center;
}
.v-btn {
  width: 100%;
}
.v-card {
  padding: 1em;
}
.green {
  margin-bottom: 1em;
}
.create {
  text-transform: capitalize;
  margin-bottom: 1em;
}
.loginOverlay {
  position: absolute;
  left: 270px;
  top: 150px;
}
.head {
  position: absolute;
  left: 170px;
  font-size: 30px;
  font-family: "Courier New";
}
.icon {
  left: 200px;
}
</style>
