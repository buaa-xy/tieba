<template>
  <v-main>
    <home-navigation></home-navigation>
    <v-container fill-height>
      <v-card
        outlined
        height="80%"
        width="85%"
        color="teal"
        :class="dark ? 'darken-4' : 'lighten-4'"
      >
        <v-row align="center" no-gutters class="setting" style="height: 100%">
          <v-item-group v-model="window" class="shrink mx-5" mandatory>
            <v-item class="ma-2 my-8">
              <div>
                <v-btn icon @click="window = 0">
                  <v-icon large v-if="window != 0">mdi-account</v-icon>
                  <v-icon large v-else>mdi-record</v-icon>
                </v-btn>
              </div>
            </v-item>
            <v-item class="ma-2 my-4">
              <div>
                <v-btn icon @click="window = 1">
                  <v-icon large v-if="window != 1">mdi-palette</v-icon>
                  <v-icon large v-else>mdi-record</v-icon>
                </v-btn>
              </div>
            </v-item>
            <v-item class="ma-2 my-8">
              <div>
                <v-btn icon @click="window = 2">
                  <v-icon large v-if="window != 2">mdi-access-point</v-icon>
                  <v-icon large v-else>mdi-record</v-icon>
                </v-btn>
              </div>
            </v-item>
          </v-item-group>
          <v-col style="height: 100%" class="mx-5">
            <v-window v-model="window">
              <v-window-item>
                <v-card-text>
                  <v-row class="ma-2" align="center">
                    <v-icon x-large left> mdi-account </v-icon>
                    <b class="text-h5">用户信息</b>
                  </v-row>
                  <v-divider></v-divider>
                </v-card-text>
                <!-- username -->
                <v-card-text>
                  <v-container class="d-flex align-center">
                    <v-hover v-slot="{ hover }">
                      <v-avatar
                        class="ma-2"
                        size="64"
                        color="green"
                        :class="{ 'show-upload': hover }"
                      >
                        <img
                          :src="'/api/img/' + avatar"
                          v-if="avatar != null"
                        />
                        <div v-else>{{ username }}</div>
                        <v-btn
                          icon
                          :class="{ 'show-btns': hover }"
                          style="position: fixed"
                          :color="transparent"
                        >
                          <v-file-input
                            v-model="file"
                            @change="upload"
                            accept="image/*"
                            class="hidden"
                          ></v-file-input>
                          <v-icon
                            :class="{ 'show-btn': hover }"
                            :color="transparent"
                            >mdi-upload</v-icon
                          >
                        </v-btn>
                      </v-avatar>
                    </v-hover>
                    <v-divider vertical class="mx-5"></v-divider>
                    <div class="ma-2">
                      <em>用户名</em>
                      <br />
                      <b style="font-size: 22px">{{ username }}</b>
                    </div>
                  </v-container>
                </v-card-text>
                <!-- nickname -->
                <v-card-text class="detail">
                  <b class="detail-title">昵称</b>
                  <v-btn plain icon @click="editor(1)">
                    <v-icon>mdi-pencil-outline</v-icon>
                  </v-btn>
                  &nbsp;
                  <div v-if="edit != 1" class="detail-text">
                    {{ this.nickname ? this.nickname : "暂未设置昵称" }}
                  </div>
                  <v-text-field
                    prepend-inner-icon="mdi-close-box"
                    @click:prepend-inner="editor(0)"
                    append-outer-icon="mdi-checkbox-outline"
                    @click:append-outer="submit(1)"
                    :loading="loading1"
                    v-else
                    v-model="text"
                  ></v-text-field>
                </v-card-text>

                <!-- email -->
                <v-card-text class="detail">
                  <b class="detail-title">邮箱</b>
                  <v-btn plain icon @click="editor(3)">
                    <v-icon>mdi-pencil-outline</v-icon>
                  </v-btn>
                  &nbsp;
                  <div v-if="edit != 3" class="detail-text">
                    {{ this.email ? this.email : "暂未设置邮箱" }}
                  </div>
                  <v-text-field
                    prepend-inner-icon="mdi-close-box"
                    @click:prepend-inner="editor(0)"
                    append-outer-icon="mdi-checkbox-outline"
                    @click:append-outer="submit(3)"
                    :loading="loading3"
                    v-else
                    v-model="text"
                  ></v-text-field>
                </v-card-text>
                <!-- password -->
                <v-card-text class="detail">
                  <b class="detail-title">密码</b>
                  <v-btn plain icon @click="editor(2)">
                    <v-icon>mdi-pencil-outline</v-icon>
                  </v-btn>
                  &nbsp;
                  <div v-if="edit != 2" class="detail-text">点击修改密码</div>
                  <v-text-field
                    prepend-inner-icon="mdi-close-box"
                    @click:prepend-inner="editor(0)"
                    append-outer-icon="mdi-checkbox-outline"
                    @click:append-outer="submit(2)"
                    :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="show ? 'text' : 'password'"
                    :rules="passwordRules"
                    @click:append="show = !show"
                    :loading="loading2"
                    v-else
                    v-model="text"
                  ></v-text-field>
                </v-card-text>
              </v-window-item>
              <v-window-item>
                <v-card-text>
                  <v-row class="ma-2" align="center">
                    <v-icon x-large left> mdi-palette </v-icon>
                    <b class="text-h5">主题设置</b>
                    <v-spacer></v-spacer>
                  </v-row>
                  <v-divider></v-divider>
                  <v-switch
                    class="ma-4"
                    color="teal"
                    v-model="dark"
                    inset
                    flat
                    hide-details="true"
                    @change="changeTheme"
                    label="深色主题"
                  ></v-switch>
                </v-card-text>
              </v-window-item>
              <v-window-item>
                <v-card-text>
                  <v-row class="ma-2" align="center">
                    <v-icon x-large left> mdi-access-point </v-icon>
                    <b class="text-h5">通知过滤</b>
                  </v-row>
                  <v-divider></v-divider>
                </v-card-text>
              </v-window-item>
            </v-window>
          </v-col>
        </v-row>
      </v-card>
    </v-container>
  </v-main>
</template>

<script>
export default {
  data() {
    return {
      window: 0,
      length: 3,
      edit: 0,
      show: false,
      loading1: false,
      loading2: false,
      loading3: false,
      dark: this.$vuetify.theme.dark,
      transparent: "rgba(255,255,255,0)",
      isLogin: false,
      file: null,
      username: null,
      nickname: null,
      avatar: null,
      email: null,
      text: "",
      passwordRules: [
        (v) => !!v || "密码未填写",
        (v) => v.length >= 6 || "密码必须为6~32位",
        (v) => v.length <= 32 || "密码必须为6~32位",
        (v) => {
          const pattern = /^(?![^a-zA-Z]+$)(?!\D+$)/;
          return pattern.test(v) || "密码必须同时包含数字与字母";
        },
      ],
    };
  },
  mounted() {
    this.$axios({
      method: "get",
      url: "/api/profile",
    })
      .then((response) => {
        if (response.data.success === true) {
          let data = response.data.data;
          if (data.active) this.isLogin = true;
          this.username = data.username;
          if (data.nickname) this.nickname = data.nickname;
          if (data.email) this.email = data.email;
          if (data.avatar) this.avatar = data.avatar;
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
  methods: {
    editor(index) {
      this.edit = index;
      if (index == 1) this.text = this.nickname;
      if (index == 2) this.text = this.password;
      if (index == 3) this.text = this.email;
    },
    submit(index) {
      let profile = {
        password: this.password,
        email: this.email,
        avatar: this.avatar,
        nickname: this.nickname,
      };
      if (index == 1) {
        this.loading1 = true;
        profile.nickname = this.text;
      }
      if (index == 2) {
        this.loading2 = true;
        profile.password = this.text;
      }
      if (index == 3) {
        this.loading3 = true;
        profile.email = this.text;
      }
      this.$axios({
        method: "post",
        url: "/api/modify/profile",
        data: profile,
      }).then((response) => {
        if (response.data.success == true) {
          console.log("modify success");
          if (index == 1) {
            this.loading1 = false;
            this.nickname = this.text;
            localStorage.setItem("nickname", this.text);
          }
          if (index == 2) {
            this.loading2 = false;
            this.password = this.text;
          }
          if (index == 3) {
            this.loading3 = false;
            this.email = this.text;
            localStorage.setItem("email", this.text);
          }
          this.editor(0);
          location.reload();
        } else {
          console.log(response.data.message);
          this.loading1 = false;
          this.loading2 = false;
          this.loading3 = false;
        }
      });
    },
    upload() {
      let formData = new FormData();
      formData.append("image", this.file);
      this.$axios({
        method: "post",
        url: "/api/img/upload",
        data: formData,
      }).then((response) => {
        if (response.data.success == true) {
          let data = response.data.data;
          this.avatar = data;
          localStorage.setItem("avatar", data.avatar);
          this.submit(4);
        } else {
          console.log(response.data.message);
        }
      });
    },
    changeTheme() {
      this.$vuetify.theme.dark = this.dark;
      if (this.dark == true) {
        document.documentElement.style.setProperty(
          "--glass-color",
          "#00000080"
        );
        document.documentElement.style.setProperty("--bg-color", "#1E1E1E");
        document.documentElement.style.setProperty("--text-color", "#b0b1ac");
        document.documentElement.style.setProperty(
          "--heading-color",
          "#cbcdbf"
        );
      } else {
        document.documentElement.style.setProperty("--glass-color", "ffffff80");
        document.documentElement.style.setProperty("--bg-color", "#ffffff");
        document.documentElement.style.setProperty("--text-color", "#777777");
        document.documentElement.style.setProperty(
          "--heading-color",
          "#484f58"
        );
      }
    },
  },
};
</script>

<style scoped>
.setting {
  height: 100%;
  font-family: "Fira code";
}
.show-upload {
  opacity: 0.6;
}
.show-btn {
  color: rgb(60, 60, 60) !important;
}
.hidden {
  opacity: 0;
  position: absolute;
  z-index: 10;
}
.detail {
  margin-left: 2em;
  font-size: 16px;
  display: flex;
  width: 600px;
}
.detail-title {
  margin-top: 6px;
}
.detail-text {
  margin-top: 6px;
  margin-bottom: 26px;
}
.v-text-field {
  margin: 0;
  padding: 0;
}
</style>
