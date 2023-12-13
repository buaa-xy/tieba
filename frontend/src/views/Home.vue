<template>
  <v-main>
    <home-navigation></home-navigation>
    <v-row no-gutters>
      <v-col cols="8" class="mx-auto">
        <base-search
          class="glass"
          id="search"
          v-on:searchResult="searchResult"
        ></base-search>
        <post-card
          v-for="post in posts"
          :key="post.id"
          :post="post"
          @refresh="getPosts()"
        ></post-card>
      </v-col>
      <v-col cols="4">
        <recommended-list :users="users"></recommended-list>
      </v-col>
    </v-row>
  </v-main>
</template>

<script>
import PostCard from "@/components/cards/PostCard.vue";
import BaseSearch from "@/components/BaseSearch.vue";

export default {
  name: "Home",
  components: {
    "post-card": PostCard,
    "base-search": BaseSearch,
  },
  data() {
    return {
      sort: "time",
      count: 20,
      posts: [],
      users: [],
    };
  },
  mounted() {
    this.getPosts();
  },
  methods: {
    searchResult(data) {
      this.posts = data;
      console.log(this.posts);
    },
    getPosts() {
      this.$axios({
        method: "get",
        url: "/api/post/getPostList",
        params: {
          sort: this.sort,
          count: this.count,
        },
      })
        .then((response) => {
          if (response.data.state == 200) {
            let data = response.data.data;
            this.posts = data;
            console.log(this.posts);
            
            //this.users = data.userList;
          } else {
            console.log(response.data.message);
          }
        })
        .catch((error) => {
          console.log(error);
        });
      this.$axios({
        method: "get",
        url: "/api/user/getUserList",

      })
        .then((response) => {
          if (response.data.state == 200) {
            let data = response.data.data;
            this.users = data;
            for (let i = 0; i < this.users.length; i++) {
              this.users[i].follow = Math.floor(Math.random() * 10);
              this.users[i].fans = Math.floor(Math.random() * 10);
              this.users[i].votes = Math.floor(Math.random() * 10);
            } 
            console.log(this.users[0])
            //this.users = data.userList;
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

<style scoped>
.glass {
  background-color: var(--glass-color);
  backdrop-filter: blur(2px);
}
</style>
