<template>
  <v-menu min-width="192px" rounded="xl" offset-y top nudge-top="5">
    <template v-slot:activator="{ on }">
      <v-btn block v-on="on" height="72px" outlined>
        <div v-if="isLogin" class="d-flex justify-between align-center">
          <v-avatar color="cyan" size="48" v-if="user.avatar == null">
            <img :src="'../../public/logo.png'" alt="!" />
          </v-avatar>
          <v-icon v-else large>mdi-account-circle</v-icon>
        </div>
        <div v-else>点击登录</div>
        <v-spacer v-if="isLogin"></v-spacer>
        <div v-if="isLogin">{{ user.username }}</div>
      </v-btn>
    </template>
    <v-card>
      <div class="mx-auto text-center py-3">
        <div v-if="isLogin">
          <b>{{ user.nickname }}</b>
          <p>@{{ user.username }}</p>
          <p class="text-caption mt-1">
            {{ user.email }}
          </p>
          <v-divider class="my-3"></v-divider>
          <v-btn text :to="'/user/' + user.uid">
          <!-- <v-btn text :to="'/user/0'"> -->
            <b>个人主页</b>
          </v-btn>
          <v-divider class="my-3"></v-divider>
          <v-btn text color="teal" @click="logout">
            <b>退出登录</b>
          </v-btn>
        </div>
        <div v-else>
          <v-icon large>mdi-account-circle</v-icon>
          <v-divider class="my-3"></v-divider>
          <user-login></user-login>
          <v-divider class="my-3"></v-divider>
          <v-btn text to="/register" v-if="!isLogin"> 注册账号 </v-btn>
        </div>
      </div>
    </v-card>
  </v-menu>
</template>

<script>
import UserLogin from "./UserLogin.vue";
export default {
  components: {
    "user-login": UserLogin,
  },
  data() {
    return {
      isLogin: false,
      user: {
        username: null,
        nickname: "nickname",
        email: null,
        avatar: null,
      },
    };
  },
  mounted() {
    console.log(localStorage.getItem("userid"));
    let username = localStorage.getItem("username");
    if (username) {
      this.user.username = localStorage.getItem("username");
      //this.user.nickname = localStorage.getItem("nickname");
      this.user.email = localStorage.getItem("email");
      this.user.avatar = localStorage.getItem("avatar");
      this.user.uid = localStorage.getItem("userid");
      this.isLogin = sessionStorage.getItem("isLogin");
      console.log(localStorage.getItem("username"));
      console.log("not post");
    } else {
      this.getuser();
      console.log("post");
    }
  },
  methods: {
    getuser() {
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
            console.log(localStorage.getItem("username"));
            // }
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
    logout() {
      localStorage.clear();
      // this.$axios({
      //   method: "post",
      //   url: "/api/logout",
      //   data: {},
      // }).then((response) => {
      //   console.log(response.data);
      //   if (response.data.success === true) {
      //     sessionStorage.setItem("isLogin", false);
      //     localStorage.clear();
      //     //登出成功
      this.$notify({
        title: "成功",
        message: "登出成功",
        type: "success",
      });
      this.$router.push("/");
      location.reload();
        // } else {
        //   //失败
        //   this.$router.push("/");
        // }
      // });
    },
  },
};
</script>

<style></style>
