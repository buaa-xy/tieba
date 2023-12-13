<template>
  <v-hover v-slot="{ hover }">
    <v-card
      class="user-card mx-auto d-flex justify-center align-center"
      min-width="200"
      max-width="300"
      :elevation="hover ? 2 : 0"
      outlined
    >
      <v-avatar class="ma-2" size="48" color="green">
        <img
          v-if="user.avatar == null"
          :src="'/api/img/' + user.avatar"
        />
        <div v-else>{{ user.username }}</div>
      </v-avatar>
      <v-sheet class="main" @click="href(user.id)">
        <div class="ma-1 user-link">
          <b>{{ user.username }}</b>
        </div>
        <div class="ma-1 details">
          <span class="mx-1">{{ user.follow }} 关注</span>
          <span class="mx-1">{{ user.fans }} 粉丝</span>
          <span class="mx-1">{{ user.votes }} 获赞</span>
        </div>
      </v-sheet>
      <v-spacer></v-spacer>
      <v-btn
        depressed 
        class="mr-2"
        @click.stop="user.followed ? cancelFollow() : follow()"
        :color="user.followed ? 'cyan' : ''"
      >
        <v-icon> mdi-star-plus </v-icon>
        关注
      </v-btn>
    </v-card>
  </v-hover>
</template>

<script>
export default {
  props: {
    user: {
      type: Object,
    },
  },
  data() {
    return {
      follow:0,
      fans:0,
      votes:3,
    };
  },
  methods: {
    href(uid) {
      this.$router.push({ path: `/user/${uid}` });
    },
    // follow() {
    //   this.$axios({
    //     method: "post",
    //     url: "/api/follow",
    //     data: {
    //       username: this.user.username,
    //     },
    //   })
    //     .then((response) => {
    //       if (response.data.success == true) {
    //         this.getData();
    //       } else {
    //         console.log(response);
    //         console.log(response.data.message);
    //       }
    //     })
    //     .catch((error) => {
    //       console.log(error);
    //     });
    // },
    // cancelFollow() {
    //   this.$axios({
    //     method: "post",
    //     url: "/api/follow/cancel",
    //     data: {
    //       username: this.user.username,
    //     },
    //   })
    //     .then((response) => {
    //       if (response.data.success == true) {
    //         this.getData();
    //       } else {
    //         console.log(response);
    //         console.log(response.data.message);
    //       }
    //     })
    //     .catch((error) => {
    //       console.log(error);
    //     });
    // },
  },
};
</script>

<style>
.user-card {
  margin: 8px 0px 8px 0px;
  text-align: left;
  display: flex;
  font-family: "Fira code";
}
.main {
  cursor: pointer;
}
.user-link:hover {
  text-decoration: underline;
}
.details {
  font-size: 12px;
}
</style>
