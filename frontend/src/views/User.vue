<template>
  <v-app id="inspire">
    <v-navigation-drawer v-model="drawer" app>
      <v-sheet color="grey lighten-4" class="pa-4">
        <v-avatar
          class="mb-4"
          color="grey darken-1"
          size="64"
          v-if="this.avatar != null"
        >
          <img :src="'/api/img/' + this.avatar" alt="!" />
        </v-avatar>

        <v-icon v-else size="64">mdi-account-circle</v-icon>
        <span>&nbsp;&nbsp;&nbsp;{{ this.username }}</span>
        <div>昵称：{{ this.nickname }}</div>
        <div>邮箱：{{ this.email }}</div>
        <div>粉丝量：{{ this.fans }}</div>
        <div>获赞量：{{ this.likes }}</div>
        <div>关注量：{{ this.follows }}</div>
      </v-sheet>

      <v-divider></v-divider>

      <v-list>
        <v-list-item link @click="postbutton">
          <v-list-item-icon>
            <v-icon>mdi-pencil</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>已发表帖子</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <!-- 
        <v-list-item link @click="commentbutton">
          <v-list-item-icon>
            <v-icon>mdi-send</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>已发表评论</v-list-item-title>
          </v-list-item-content>
        </v-list-item> -->
        <v-list-item link @click="returnhome">
          <v-list-item-icon>
            <v-icon>mdi-home</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>返回主页</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-main>
      <v-container class="py-8 px-6" fluid>
        <v-row>
          <v-col cols="12">
            <v-card v-if="isshow">
              <v-subheader>帖子</v-subheader>

              <v-list two-line>
                <v-list-item
                  v-for="post in posts"
                  :key="post.id"
                  :post="post"
                  @click="href(post.pid)"
                >
                  <v-list-item-avatar color="cyan">
                    <v-icon>mdi-text-box-multiple-outline</v-icon>
                  </v-list-item-avatar>

                  <v-list-item-content>
                    <v-list-item-title>{{ post.title }}</v-list-item-title>

                    <v-list-item-subtitle>
                      <span class="block1"
                        >发表时间：{{ (post.date.substring(0,10)) }}
                      </span>
                      <span>&nbsp;&nbsp;</span>
                      <span class="block2">获赞量：{{ post.comments+1 }} </span>
                      <span class="block2">评论数：{{ post.comments }}</span>
                      <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                      <span class="block2" v-if="post.type==0"  style="color:red;">待审核</span>
                      <span class="block2" v-else>已发布</span>
                      <v-btn v-if="ismine" outlined @click.stop="editbutton(post.postId)"
                        ><v-icon>mdi-send</v-icon>&ensp;编辑</v-btn
                      >
                    </v-list-item-subtitle>
                  </v-list-item-content>
                </v-list-item>
              </v-list>
            </v-card>

            <v-card v-else>
              <v-subheader>我的评论</v-subheader>

              <v-list two-line>
                <v-list-item>
                  <v-list-item-avatar color="pink lighten-3">
                    <v-icon>mdi-message-text</v-icon>
                  </v-list-item-avatar>

                  <v-list-item-content>
                    <v-list-item-title>你评论了：xxx</v-list-item-title>
                    <br />
                    <v-text>XXXXXXXXxxx</v-text>
                    <br />
                    <v-list-item-subtitle>
                      <span class="block1">发表时间：2021 </span>
                      <span class="block2">获赞量：12 </span>
                      <span class="block2">回复数：33</span>
                    </v-list-item-subtitle>
                  </v-list-item-content>
                </v-list-item>
              </v-list>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
export default {
  // mounted() {
  //   this.getPosts();
  // },
  data() {
    return {
      ismine: false,
      userId: null,
      isshow: true,
      username: null,
      posts: [],
      records: [],
      fans: null,
      follows: null,
      likes: null,
      avatar: null,
      email: null,
      nickname: null,
      cards: ["Today"],
      drawer: null,
      links: [
        ["mdi-pencil", "已发表帖子"],
        ["mdi-send", "已发表评论"],
        ["mdi-home", "返回主页"],
      ],
    };
  },
  created() {
    this.$watch(
      () => this.$route.params.userId,
      () => {
        this.getData();
        console.log(this.$route.params.userId);
      },
      { immediate: true }
    );
  },
  methods: {
    editbutton(pid) {
      console.log(pid);
      this.$router.push({ path: `/edit/${pid}` });
    },
    href(pid) {
      console.log(pid);
      this.$router.push({ path: `/post/${pid}` });
    },
    postbutton() {
      this.isshow = true;
    },
    commentbutton() {
      this.isshow = false;
    },
    returnhome() {
      this.$router.push("/");
    },
    time(thetime) {
      return this.timeFormat(thetime);
    },
    getData() {
      this.userId = this.$route.params.userId;
      if (this.userId == 0) {
        this.userId = null;
        this.ismine = true;
      }
      this.$axios({
        method: "get",
        url: "/api/user/getUserInfoByUid",
        params: {
        
          uid: this.userId,
        },
      })
        .then((response) => {
          if (response.data.state === 200) {
            let data = response.data.data;
            this.username = data.username;
            //this.posts = data.posts;
            this.nickname = "nickname";
            //this.records = data.records;
            this.fans = 3;//Math.floor(Math.random() * 10);
            this.follows = 2;//Math.floor(Math.random() * 10);
            this.likes = 4;//Math.floor(Math.random() * 10);
            //this.avatar = data.avatar;
            //console.log(this.avatar);
            this.email = data.email;
            console.log(response);
          } else {
            console.log(response);
            console.log(response.data);
          }
        })
        .catch((error) => {
          console.log(error);
        });

      this.$axios({
        method: "get",
        url: "/api/post/getPostListByUid",
        params: {
          uid: this.userId,
        },
      })
        .then((response) => {
          if (response.data.state === 200) {
            let data = response.data.data;
            
            this.posts = data;
            console.log(data);
          
          } else {
            console.log(response);
            console.log(response.data);
          }
        })
        .catch((error) => {
          console.log(error);
        });

    },
    getPosts() {
      this.$axios({
        method: "get",
        url: "/api/explore",
        params: {
          sort: "heat",
          count: 100,
        },
      })
        .then((response) => {
          if (response.data.success == true) {
            let data = response.data.data;
            this.posts = data.postList;
            this.users = data.userList;
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

<style>
.roundIMG {
  width: 200px;
  height: 200px;
  display: flex;
  border-radius: 10%;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  float: left;
}
.center {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
}
.likes {
  position: relative;
  left: 500px;
  top: 00px;
}
.block1 {
  display: inline-block;
  width: 150px;
}
.block2 {
  display: inline-block;
  width: 100px;
}
</style>
