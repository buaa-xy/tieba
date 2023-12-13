<template>
  <v-card class="mx-auto" :dark="dark" max-width="600" min-width="500">
    <v-card-text class="pb-0">
      <span v-for="thefollow in users" :key="thefollow">
        <v-card-title>
          <v-avatar color="cyan" size="24">
            <!-- 放头像？ -->
            <img :src="'/api/img/' + thefollow.avatar" alt="!" />
          </v-avatar>

          <span class="font-weight-light" algin="center"
            >&nbsp;&nbsp;&nbsp;{{ thefollow.username }}</span
          >
          <v-spacer></v-spacer>
          <v-btn
            class="ma-2"
            outlined
            color="indigo"
            @click="cancelFollow(thefollow.username)"
          >
            取消关注
          </v-btn>
        </v-card-title>

        <v-divider class="mx-4" color="white"></v-divider>
        <br />
      </span>
    </v-card-text>
  </v-card>
</template>

<script>
export default {
  props: {
    //之后改成users
    users: {
      type: Object,
      default: () => {},
    },
  },
  data() {
    return {
      dark: false,
      expand: false,
      cancel: "",
      reply: ["a", "s", "as"],
      user: [
        {
          name: "username111",
          email: "123123@buaa.edu.cn",
          images: "头像",
        },
        {
          name: "aads",
          email: "13333@qq.com",
          images: "头像",
        },
        {
          name: "dsadasd",
          email: "ccccc@164.com",
          images: "头像",
        },
      ],
    };
  },
  methods: {
    cancelFollow(username) {
      this.$axios({
        method: "post",
        url: "/api/follow/cancel",
        data: {
          username: username,
        },
      })
        .then((response) => {
          if (response.data.success == true) {
            console.log("success");
          } else {
            console.log(response);
            console.log(response.data.message);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    href(type, id) {
      if (id == null) {
        this.$notify({
          title: "数据缺失",
          message: "信息暂未收录，给您带来不便敬请谅解。",
          type: "warning",
        });
        return;
      }
      var this_path = this.$route.path.substring(1);
      var this_id = "";
      if (this.$route.query.id != null) this_id = this.$route.query.id;
      else if (this.$route.query.name != null) this_id = this.$route.query.name;
      if (type == this_path && id == this_id) return;
      if (type == "topic" || type == "subject") {
        let { href } = this.$router.resolve({
          path: type,
          query: { name: id.replaceAll("<b>", "").replaceAll("</b>", "") },
        });
        window.open(href, "_blank");
      } else {
        let { href } = this.$router.resolve({ path: type, query: { id: id } });
        window.open(href, "_blank");
      }
    },
  },
};
</script>

<style>
.small {
  height: 10px;
  width: 10px;
  background-size: 10%;
  overflow: hidden;
}
.reply {
  font-size: 15px;
}
</style>
