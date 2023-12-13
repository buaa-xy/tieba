<template>
  <v-card class="mx-auto">
    <v-container>
      <v-avatar color="cyan" size="24">
        <img :src="'/api/img/' + comment.avatar" />
      </v-avatar>
      <a class="mx-2" :href="'/user/' + comment.userId">
        <b>{{ comment.nickname }}</b>
      </a>
    </v-container>
    <div class="mx-4">
      <span v-if="detail" v-html="comment.content"></span>
      <span v-else v-html="$options.filters.abstarct(comment.content)"></span>
      <v-btn
        icon
        v-if="comment.content && comment.content.length > 120"
        @click="detail = !detail"
      >
        <v-icon v-if="detail">mdi-chevron-up</v-icon>
        <v-icon v-else>mdi-chevron-down</v-icon>
      </v-btn>
    </div>
    <v-card-actions>
      <v-list-item class="grow">
        <v-row align="center">
          <small v-text="timeFormat(comment.time)"></small>
          <v-btn
            plain
            small
            @click="vote(1)"
            :color="comment.voteValue == 1 ? 'cyan' : ''"
          >
            <v-icon small> mdi-thumb-up </v-icon>
            &nbsp;{{ comment.votes != 0 ? comment.votes : "" }}
          </v-btn>
          <v-btn
            small
            icon
            @click="vote(-1)"
            :color="comment.voteValue == -1 ? 'cyan' : ''"
          >
            <v-icon small> mdi-thumb-down </v-icon>
          </v-btn>
          <v-btn icon @click="replyIn(null, comment.nickname)">
            <v-icon small> mdi-message-text </v-icon>
          </v-btn>
          <v-spacer></v-spacer>
          <v-btn
            v-if="comment.replies.length > 0"
            outlined
            @click="expand = !expand"
          >
            <span v-if="expand">收起回复</span>
            <span v-else @click="show">查看回复</span>
            <v-icon v-if="expand">mdi-chevron-up</v-icon>
            <v-icon v-else>mdi-chevron-down</v-icon>
          </v-btn>
        </v-row>
      </v-list-item>
    </v-card-actions>
    <v-container v-if="expand">
      <v-container v-for="(reply, i) in comment.replies" :key="reply.replyId">
        <v-avatar class="ma-2" size="20" color="green">
          <img v-if="reply.avatar != null" :src="'/api/img/' + reply.avatar" />
          <div v-else>{{ reply.username }}</div>
        </v-avatar>
        <a class="mx-1" :href="'/user/' + reply.userId">
          <b>{{ reply.nickname }}</b>
        </a>
        <small class="mx-1">回复</small>
        <a v-if="reply.replyToUserId" :href="'user/' + reply.replyToUserId">@{{ reply.replyToNickname }} </a>
        <div class="mx-2">
          <div>
            <span v-if="replyDetail[i]" v-text="reply.content"></span>
            <span
              v-else
              v-text="$options.filters.abstarct(reply.content)"
            ></span>
            <v-btn
              icon
              v-if="reply.content && reply.content.length > 120"
              @click="replyExpand(i)"
            >
              <v-icon v-if="replyDetail[i]">mdi-chevron-up</v-icon>
              <v-icon v-else>mdi-chevron-down</v-icon>
            </v-btn>
          </div>
          <div>
            <small v-text="timeFormat(reply.time)"></small>
            <v-btn icon @click="replyIn(reply.replyId, reply.nickname)">
              <v-icon small> mdi-message-text </v-icon>
            </v-btn>
          </div>
        </div>
      </v-container>
      <v-divider></v-divider>
    </v-container>
    <v-container v-if="replyArea" class="d-flex align-center mt-auto">
      <v-avatar class="ma-2" size="36" color="green">
        <img v-if="avatar != null" :src="'/api/img/' + avatar" />
        <div v-else>{{ username }}</div>
      </v-avatar>
      <v-textarea
        color="cyan"
        class="mr-2"
        @focus="focus = true"
        @blur="focus = false"
        :rows="focus ? 4 : 1"
        dense
        outlined
        no-resize
        style="font-size: 14px"
        :placeholder="'回复@' + toname + ':'"
        hide-details
        append-icon="mdi-send-circle"
        @click:append="submit()"
        append-outer-icon="mdi-close-circle"
        @click:append-outer="replyArea = false"
        v-model="text"
      >
      </v-textarea>
    </v-container>
  </v-card>
</template>

<script>
export default {
  props: ["comment"],
  data() {
    return {
      dark: false,
      expand: false,
      show: false,
      detail: false,
      focus: false,
      text: null,
      replyDetail: [],
      toname: null,
      toId: null,
      replyArea: false,
      avatar: localStorage.getItem("avatar")
    };
  },
  mounted() {
    this.replyDetail = new Array(this.comment.replies.length).fill(false);
  },
  filters: {
    abstarct(text) {
      if (!text) return " ";
      if (text.length > 120) {
        return text.slice(0, 110) + "...";
      }
      return text;
    },
  },
  methods: {
    replyExpand(index) {
      this.$set(this.replyDetail, index, !this.replyDetail[index]);
    },
    replyIn(id, name) {
      this.replyArea = true;
      this.toname = name;
      this.toId = id;
    },
    vote(value) {
      this.$axios({
        method: "post",
        url: "/api/vote",
        data: {
          id: this.comment.commentId,
          value: value,
        },
      })
        .then((response) => {
          if (response.data.success === true) {
            this.$emit("update");
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
        url: "/api/reply",
        data: {
          commentId: this.comment.commentId,
          replyToId: this.toId == null ? undefined : this.toId,
          content: this.text,
        },
      })
        .then((response) => {
          if (response.data.success === true) {
            this.$emit("update");
          } else {
            console.log(response);
            console.log(response.data);
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
.small {
  height: 10px;
  width: 10px;
  background-size: 10%;
  overflow: hidden;
}
.reply {
  font-size: 15px;
}
a:link {
  text-decoration: none;
  color: var(--text-color);
}
a:visited {
  text-decoration: none;
  color: var(--text-color);
}
a:hover {
  color: #15987e;
  text-decoration: underline;
}
</style>
