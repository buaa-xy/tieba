<template>
  <v-main class="background">
    <v-container class="post">
      <div class="left">
        <div class="title">
          <v-btn
            plain
            icon
            fixed
            left
            @click="(allowAnnotate = !allowAnnotate), (hover = -1)"
          >
            <v-icon :color="allowAnnotate ? 'cyan' : null"
              >mdi-square-edit-outline</v-icon
            >
          </v-btn>
          <b style="font-size: 24px">{{ post.title }}</b>
          <v-btn tile icon to="/home" fixed right>
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </div>
        <!-- <v-sheet
          v-for="(block, index) in blocks"
          :key="block.blockId"
          @mouseover="hover = index"
        > -->
          <v-sheet
            class="block"
            v-html="post.text"
            :class="{
              'on-hover': index == hover || (index > hover && index < end),
              'not-no-hover': hover == -1,
            }"
          ></v-sheet>
        <!-- </v-sheet> -->
        <v-divider class="my-4"></v-divider>
        <v-banner
          color="cyan"
          single-line
          class="ma-2 rounded-lg d-flex"
        >
          <v-icon> mdi-message-outline </v-icon>
          <b>评论</b>
          <v-spacer></v-spacer>
          <template v-slot:actions>
            <v-btn text @click="sortBy('hot')">
              <b v-if="sort == 'hot'">最热</b>
              <span v-else>最热</span>
            </v-btn>
            <v-divider vertical></v-divider>
            <v-btn text @click="sortBy('new')">
              <b v-if="sort == 'new'">最新</b>
              <span v-else>最新</span>
            </v-btn>
          </template>
        </v-banner>
        <v-sheet class="comment ma-4">
          <v-avatar class="ma-2" size="36" color="green">
            <img v-if="avatar != null" :src="'/api/img/' + avatar" />
            <div v-else>{{ username }}</div>
          </v-avatar>
          <v-textarea
            color="cyan"
            class="mr-2"
            @focus="focus1 = true"
            @blur="focus1 = false"
            :rows="focus1 ? 4 : 1"
            dense
            outlined
            no-resize
            style="font-size: 14px"
            placeholder="发一条评论"
            hide-details
            append-icon="mdi-send-circle"
            @click:append="submit()"
            v-model="text"
          ></v-textarea>
        </v-sheet>
        <comment-card
          v-for="comment in comments"
          :key="comment.commentId"
          :comment="comment"
          @update="getData()"
        ></comment-card>
        <v-container
          v-if="comments.length == 0"
          class="ma-2 text-center"
        >没有更多评论
        </v-container>
      </div>
      <div class="right">
        <div style="position: fixed; bottom: 0; top: 0">
          <!-- <annotation-card
            v-if="hover != -1"
            @getClose="hover = -1"
            @update="getData()"
            :annotations="annotations"
            :blockId="blocks[hover].block.blockId"
          ></annotation-card> -->
          <v-card outlined class="infomation">
            <v-sheet class="d-flex align-center">
              <v-avatar class="ma-2" size="48" color="green">
                <img
                  v-if="author.avatar == null"
                  :src="'/api/img/' + author.avatar"
                />
                <div v-else>{{ author.username }}</div>
              </v-avatar>
              <v-sheet>
                <div class="ma-1">
                  <b>{{ author.username }}</b>
                </div>
                <div class="ma-1">
                  <small>{{ time }}</small>
                </div>
              </v-sheet>
              <v-spacer></v-spacer>
              <v-btn plain text :to="'/user/'+author.userId">
                <v-icon large> mdi-text-box-multiple-outline </v-icon>
                主页
              </v-btn>
              <v-btn plain text 
                @click="author.followed ? cancelFollow() : follow()"
                :color="author.followed ? 'cyan' : ''"
              >
                <v-icon large> mdi-star-plus </v-icon>
                关注
              </v-btn>
            </v-sheet>
            <v-sheet class="d-flex justify-space-around my-auto">
              <v-sheet class="mx-1">
                <v-icon> mdi-message </v-icon>
                <span>{{ post.comments }}</span>
              </v-sheet>
              <v-divider vertical></v-divider>
              <v-btn
                plain
                depressed
                @click="like(1)"
                :color="post.likeValue == 1 ? 'cyan' : ''"
              >
                <v-icon> mdi-thumb-up </v-icon>
                &nbsp;{{ post.likes != 0 ? post.likes : ""}}
              </v-btn>
              <v-btn
                plain
                depressed
                @click="like(-1)"
                :color="post.likeValue == -1 ? 'cyan' : ''"
              >
                <v-icon> mdi-thumb-down </v-icon>
              </v-btn>
              <v-btn
                plain
                depressed
                @click="favor()"
                :color="post.favored ? 'cyan' : ''"
              >
                <v-icon> mdi-heart </v-icon>
                {{ post.favored ? "取消收藏" : "收藏" }}
              </v-btn>
            </v-sheet>
            <v-sheet class="comment mt-auto">
              <v-avatar class="ma-2" size="36" color="green">
                <img v-if="avatar != null" :src="'../../public/logo.png'" />
                <div v-else>{{ username }}</div>
              </v-avatar>
              <v-textarea
                color="cyan"
                class="mr-2"
                @focus="focus2 = true"
                @blur="focus2 = false"
                :rows="focus2 ? 4 : 1"
                dense
                outlined
                no-resize
                style="font-size: 14px"
                placeholder="发一条评论"
                hide-details
                append-icon="mdi-send-circle"
                @click:append="submit()"
                v-model="text"
              ></v-textarea>
            </v-sheet>
          </v-card>
        </div>
      </div>
    </v-container>
  </v-main>
</template>

<script>
import CommentCard from "@/components/cards/CommentCard.vue";
//import AnnotationCard from "@/components/cards/AnnotationCard.vue";
import { Marked } from "@ts-stack/markdown";

export default {
  components: {
    CommentCard,
    //AnnotationCard,
  },
  data() {
    return {
      pid: null,
      hover: -1,
      end: -1,
      sort: "hot",
      title:"2",
      post: {
        title: "",
        uid: null,
      },
      author: {
        username: null,
        avatar: null,
        follows: null,
        fans: null,
        likes: null,
        followed: null
      },
      annotations: [],
      comments: [],
      blocks: null,
      allowAnnotate: true,
      text: null,
      focus1: false,
      focus2: false,
      username: localStorage.getItem("username"),
      avatar: null,
    };
  },
  created() {
    this.$watch(
      () => this.$route.params.pid,
      () => {
        this.getData();
        //console.log(localStorage.getItem("username"))
      },
      { immediate: true }
    );
  },
  computed: {
    time: function () {
      return this.post.date.substring(0,10);
    },
  },
  watch: {
    hover() {
      if (!this.allowAnnotate) {
        this.hover = -1;
        return;
      }
      if (this.hover == -1) {
        this.end = -1;
        this.annotations = null;
      } else {
        var firstSonId = this.blocks[this.hover].block.firstSonId;
        var nextBrotherId = this.blocks[this.hover].block.nextBrotherId;
        if (nextBrotherId == -1 && firstSonId != -1)
          this.end = this.blocks.length;
        else
          this.end = nextBrotherId;
        this.annotations = this.blocks[this.hover].annotationInfos;
      }
    },
  },
  methods: {
    getData() {
      this.pid = this.$route.params.pid;
      console.log(this.pid);
      this.$axios({
        method: "get",
        url: "/api/post/postInfo",
        params: {
          pid: this.$route.params.pid,
        },
      })
        .then((response) => {
          if (response.data.state === 200) {
            let data = response.data.data;
            this.post = data.post;
            this.title = data.post.title;
            console.log(this.post);
            this.getAuthor();
            this.post.text = Marked.parse(data.post.text);
            // this.blocks = data.post.blockUnits;
            // if (data.post.annotationAllowed) {
            //   this.blocks.forEach((item) => {
            //     item.block.content = Marked.parse(item.block.content);
            //   });
            // }
            //this.comments = data.comments;
            //this.sortBy(this.sort);
          } else {
            console.log(response);
            console.log(response.data);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    getAuthor() {
      this.$axios({
        method: "get",
        url: "/api/user/getUserInfoByUid",
        params: {
          uid: this.post.uid,
        },
      })
        .then((response) => {
          if (response.data.state === 200) {
            this.author = response.data.data;
            console.log(this.author);
          } else {
            console.log(response);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    submit() {
      this.$axios({
        method: "post",
        url: "/api/comment",
        data: {
          postId: this.post.postId,
          content: this.text,
        },
      })
        .then((response) => {
          if (response.data.success === true) {
            this.text = null;
            this.focus1 = false;
            this.focus2 = false;
            this.$notify({
              title: "评论成功",
              type: "success"
            })
            this.getData();
          } else {
            console.log(response);
            console.log(response.data);
          }
        })
        .catch((error) => {
          console.log(error);
        });
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
            this.getData();
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
            this.getData();
          } else {
            console.log(response);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    follow() {
      this.$axios({
        method: "post",
        url: "/api/follow",
        data: {
          username: this.author.username,
        },
      })
        .then((response) => {
          if (response.data.success == true) {
            this.getData();
          } else {
            console.log(response);
            console.log(response.data.message);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    cancelFollow() {
      this.$axios({
        method: "post",
        url: "/api/follow/cancel",
        data: {
          username: this.author.username,
        },
      })
        .then((response) => {
          if (response.data.success == true) {
            this.getData();
          } else {
            console.log(response);
            console.log(response.data.message);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    sortBy(type) {
      this.sort = type;
      if (type == 'hot')
        this.comments.sort(function(a, b){return parseInt(b.votes) - parseInt(a.votes)})
      if (type == 'new')
        this.comments.sort(function(a, b){return parseInt(b.time) - parseInt(a.time)})
    }
  },
};
</script>

<style scoped lang="scss">
@import "@/assets/styles/block.scss";
.background {
  background-color: rgba(158, 158, 158, 0.5);
}
.post {
  display: flex;
  justify-content: space-between;
  width: 78%;
  font-family: "Fira code";
}
.title {
  position: sticky;
  top: 0;
  z-index: 10;
  text-align: center;
  padding: 0.5em 0 0.5em;
  border-radius: 10px;
  background-color: var(--glass-color);
  backdrop-filter: blur(2px);
}
.on-hover {
  border-right: 8px solid #00bdbd;
}
.not-no-hover {
  border-right: 8px solid var(--bg-color);
}
.left {
  width: calc(66% + 8px);
  background-color: var(--bg-color);
  border-radius: 10px;
  word-wrap: break-word;
  word-break: normal;
}
.right {
  width: 30%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}
.infomation {
  height: calc(40% - 24px);
  margin: 12px;
  padding: 10px;
  background-color: var(--bg-color);
  border-radius: 4px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.comment {
  display: flex;
  align-items: center;
}
</style>
