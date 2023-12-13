<template>
  <v-dialog v-model="dialog" persistent max-width="600px" class="dialog">
    <template v-slot:activator="{ on, attrs }">
      <v-btn text color="teal" v-bind="attrs" v-on="on">
        <b>立即登录</b>
      </v-btn>
    </template>
    <v-card class="dialog">
      <div>
        <v-img
          class="left  box"
          height="350"
          width="230"
          src="../assets/login.svg"
        ></v-img>
        <p class="word">Mark++</p>
        <!-- <p class="bbb">Explore the unkown<br> world togrther</p> -->
      </div>
      <v-card-title>
        <span class="headline">登录</span>
        <v-spacer></v-spacer>
        <v-btn color="red darken-1" text @click="closeDialog()">X</v-btn>
      </v-card-title>
      <v-card-text>
        <v-container>
          <v-row>
            <v-spacer></v-spacer>
            <v-col cols="11">
              <v-text-field
                v-model="username"
                label="用户名"
                :rules="usernameRules"
                required
              ></v-text-field>
            </v-col>
          </v-row>
          <v-row>
            <v-spacer></v-spacer>
            <v-col cols="11">
              <v-text-field
                v-model="password"
                label="密码"
                required
                
                :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
                :type="show ? 'text' : 'password'"
                @click:append="show = !show"
              ></v-text-field>
            </v-col>
          </v-row>
        </v-container>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="deep-purple lighten-2" text> 忘记密码 </v-btn>
        <v-btn color="deep-purple lighten-2" text @click="login()">
          登录
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  data() {
    return {
      num: 0,
      token: "",
      code: "",
      username: "",
      password: "",
      findEmail: "",
      newPassword: "",
      isLogin: false,
      isRouterAlive: true,
      isFindPassword: false,
      inputNewPassword: false,
      dialog: false,
      show: false,
      passwordRules: [
        (v) => !!v || "密码未填写",
        (v) => v.length >= 6 || "密码必须为6~32位",
        (v) => v.length <= 32 || "密码必须为6~32位",
      ],
      usernameRules: [
        (v) => !!v || "用户名未填写",
        (v) => v.length <= 10 || "用户名长度不超过10",
      ],
    };
  },

  methods: {
    closeDialog() {
      this.dialog = false;
    },
    login() {
      if (this.username === "" || this.password === "") {
        console.log("error");
      } else {
        console.log(this.username);
          console.log(this.password);
        this.$axios({
          method: "get",
          url: "/api/user/login",
          params: {
            uid: this.username,
            password: this.password,
          },
        }).then((response) => {
          console.log(this.username);
          console.log(this.password);
          console.log(response.data);
          if (response.data.state === 200) {
            this.dialog = false;
            this.isLogin = true;
            sessionStorage.setItem("isLogin", true);
            localStorage.setItem("userid", response.data.data.user.uid);
            console.log(localStorage.getItem("userid"));
            this.profile();
            this.$notify({
              title: "成功",
              message: "登录成功",
              type: "success",
            });
            //成功
            this.$router.push("/");
            location.reload();
            console.log(this.isLogin);
          } else {
            //失败
            this.$notify({
              title: "失败",
              message: "登录失败，请检查用户名密码是否正确",
              type: "success",
            });
            this.$router.push("/");
          }
        });
      }
    },
    profile() {
      this.$axios({
        method: "get",
        url: "/api/user/getUserInfoByUid",
        params: {
            uid: localStorage.getItem("userid"),
          },
      })
        .then((response) => {
          if (response.data.state === 200) {
            let data = response.data.data;
            this.isLogin = true;
            this.user.username = data.username;
            //this.user.nickname = data.nickname;
            this.user.email = data.email;
            this.user.avatar = data.avatar;
            // if (data.active === false) {
            //   this.isLogin = false;
            // } else {
            localStorage.setItem("username", data.username);
            //localStorage.setItem("nickname", data.nickname);
            localStorage.setItem("email", data.email);
            localStorage.setItem("avatar", data.avatar);
            console.log(localStorage.getItem("userid"));
            //}
          } else {
            this.isLogin = false;
            console.log(response);
            console.log(response.data);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
};
</script>

<style>
.dialog {
  height: 350px;
}
.left {
  float: left;
  opacity: 0.9;
  background-color: #050000;
  position: relative;
  background-size: 100% 100%;
  background-attachment: fixed;
}
.image {
  position: absolute;
  left: 10px;
  top: 10px;
  width: 50px;
  height: 50px;
}
.box {
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
}
.word {
  position: absolute;
  left: 68px;
  top: 16px;
  color: white;
  font-family: "Courier New";
  font-size: 30px;
}
.headline {
  position: absolute;
  left: 265px;
}
</style>
