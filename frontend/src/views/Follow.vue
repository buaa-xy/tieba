<template>
  <v-main>
    <home-navigation></home-navigation>
    <v-row no-gutters style="height: 100%">
      <v-col cols="8" class="mx-auto">
        <v-card max-width="900" height="100%">
          <v-tabs
            color="deep-purple accent-4"
            right
            v-model="tab"
            icons-and-text
          >
            <!-- <v-tabs
      v-model="tab"
      centered
      dark
      icons-and-text
    > -->
            <v-tabs-slider></v-tabs-slider>
            <v-tab>关注用户</v-tab>
            <v-tab>关注帖子</v-tab>
          </v-tabs>
          <v-tabs-items v-model="tab">
            <v-tab-item>
              <follow-card :users="users"></follow-card>
            </v-tab-item>
            <v-tab-item>
              <post-card
                v-for="post in posts"
                :key="post.id"
                :post="post"
              ></post-card>
            </v-tab-item>
          </v-tabs-items>
        </v-card>
      </v-col>

      <v-col cols="3">
        <recommended-list :users="users"></recommended-list>
      </v-col>
      <!-- <button @click="followUser">关注</button> -->
    </v-row>
  </v-main>
</template>

<script>
import FollowCard from "@/components/cards/FollowCard.vue";
import PostCard from "@/components/cards/PostCard.vue";
export default {
  name: "Follow",
  components: {
    "follow-card": FollowCard,
    "post-card": PostCard,
  },
  data() {
    return {
      tab: null,
      users: [],
      posts: [],
      count: 10,
      sort: "heat",
      username: "1234",
    };
  },
  mounted() {
    this.getFollows();
    this.getFavorites();
  },
  methods: {
    followUser() {
      this.$axios({
        method: "post",
        url: "/api/follow",
        data: {
          username: this.username,
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
    getFavorites() {
      this.$axios({
        method: "get",
        url: "/api/favorites",
        params: {},
      })
        .then((response) => {
          if (response.data.success == true) {
            console.log(response);
            let data = response.data.data;
            this.posts = data;
          } else {
            console.log(response);
            console.log(response.data.message);
          }
        })
        .catch((error) => {
          console.log(error);
          console.log(1);
        });
    },
    getFollows() {
      this.$axios({
        method: "get",
        url: "/api/follow/all",
        params: {},
      })
        .then((response) => {
          if (response.data.success == true) {
            console.log(response);
            let data = response.data.data;
            this.users = data;
          } else {
            console.log(response.data.message);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
};
</script>

<style></style>
