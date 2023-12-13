<template>
  <v-hover v-slot="{ hover }">
    <v-card
      class="post-card mx-auto"
      min-width="500"
      max-width="600"
      :elevation="hover ? 4 : 2"
      v-if="!hidden"
    >
      <v-sheet
        class="vote text-center mr-2"
        tile
        :class="dark ? 'grey darken-3' : 'grey lighten-3'"
      >
        <div>
          <v-btn icon @click="like(1)" small>
            <v-icon
              :color="post.likeValue == 1 ? 'cyan' : ''"
            >mdi-arrow-up-bold-box-outline</v-icon>
          </v-btn>
        </div>
        <div class="vote-number">
          {{ post.likes }}
        </div>
        <div>
          <v-btn icon text @click="like(-1)" small>
            <v-icon 
              :color="post.likeValue == -1 ? 'cyan' : ''"
            >mdi-arrow-down-bold-box-outline</v-icon>
          </v-btn>
        </div>
      </v-sheet>
      <v-sheet class="main" width="100%" @click="href(post.pid)">
        <div class="text-h6 ma-1">
          <b>{{ post.title }}</b>
        </div>
        <div class="text-caption ma-1">
          <v-avatar
            class="ma-2"
            size="24"
            color="green"
          >
            <img
              :src="'/api/img/' + post.avatar"
              v-if="post.avatar != null"
            />
            <div v-else>{{ post.username }}</div>
          </v-avatar>
          <span class="user-link" @click.stop="$router.push({ path: `/user/${post.userId}` });">{{ post.nickname }}</span>
          发布于&nbsp;{{ time }}
        </div>
        <div class="mb-1">
          <v-btn outlined small class="ma-1">
            <v-icon>mdi-comment-multiple-outline</v-icon>
            {{ post.comments }}评论
          </v-btn>
          <v-btn outlined small class="ma-1" 
            :color="post.favored ? 'cyan' : ''"
            @click.stop="favor()"
          >
            <v-icon>mdi-star-plus-outline</v-icon>
            收藏
          </v-btn>
          <v-btn outlined small class="ma-1" @click.stop="hidden = true">
            <v-icon>mdi-eye-off-outline</v-icon>
            隐藏
          </v-btn>
        </div>
      </v-sheet>
    </v-card>
  </v-hover>
</template>

<script>
export default {
  props: {
    post: {
      type: Object,
    },
  },
  data() {
    return {
      dark: this.$vuetify.theme.dark,
      hidden: false,
    };
  },
  computed: {
    time: function () {
      console.log(this.post.date);
      console.log(this.post.uid);
      return this.post.date.substring(0,10);
      
    },
  },
  methods: {
    href(pid) {
      console.log(pid);
      this.$router.push({ path: `/post/${pid}` });
    },
    like(value) {
      this.$axios({
        method: "post",
        url: "/api/like",
        data: {
          id: this.post.postId,
          value: value,
        },
      })
        .then((response) => {
          if (response.data.success === true) {
            this.$emit("refresh");
          } else {
            console.log(response);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    favor() {
      let url = "";
      if (this.post.favored) url = "/api/favor/cancel";
      else url = "/api/favor";
      this.$axios({
        method: "post",
        url: url,
        data: {
          postId: this.post.postId,
        },
      })
        .then((response) => {
          if (response.data.success === true) {
            this.$emit("refresh");
          } else {
            console.log(response);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
};
</script>

<style socped>
.post-card {
  margin: 8px 0px 8px 0px;
  text-align: left;
  display: flex;
  font-family: "Fira code";
}
.vote {
  width: 36px;
}
.vote-number {
  font-size: 11px;
}
.user-link:hover {
  text-decoration: underline;
}
.main {
  cursor: pointer;
}
</style>
