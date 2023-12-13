<template>
  <v-btn rounded text color="teal" v-bind="attrs" v-on="on" @click="logout">
    <b>退出登录</b>
  </v-btn>
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
      passwordRules: [
        (v) => !!v || "密码未填写",
        (v) => v.length >= 6 || "密码必须为6~32位",
        (v) => v.length <= 32 || "密码必须为6~32位",
        (v) => {
          const pattern = /^(?![^a-zA-Z]+$)(?!\D+$)/;
          return pattern.test(v) || "密码必须同时包含数字与字母";
        },
      ],
      usernameRules: [
        (v) => !!v || "用户名未填写",
        (v) => v.length <= 10 || "用户名长度不超过10",
        (v) => {
          const pattern = /^[0-9A-Za-z]+$/;
          return pattern.test(v) || "用户名只能包含数字字母";
        },
      ],
    };
  },

  methods: {
    logout() {
      this.$axios({
        method: "post",
        url: "/api/logout",
        data: {},
      }).then((response) => {
        console.log(response.data);
        if (response.data.success === true) {
          sessionStorage.setItem("isLogin", false);
          //登出成功
          this.$notify({
            title: "成功",
            message: "登出成功",
            type: "success",
          });
          this.$router.push("/");
          location.reload();
        } else {
          //失败
          this.$router.push("/");
        }
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
