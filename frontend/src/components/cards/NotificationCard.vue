<template>
  <v-hover v-slot="{ hover }">
    <v-card
      class="d-flex justify-center align-center mx-8 my-4"
      outlined
      :elevation="hover ? 4 : 0"
      color="teal"
      :class="dark ? 'darken-1' : 'lighten-4'"
      :style="message.read ? 'opacity: 0.6;' : 'none'"
    >
      <v-avatar class="ma-2" size="48" color="green">
        <img
          v-if="message.user.avatar != null"
          :src="'/api/img/' + message.user.avatar"
        />
        <div v-else>{{ message.user.username }}</div>
      </v-avatar>
      <v-col class="main" @click="href(message.postId)">
        <div class="ma-1">
          <b>{{ message.user.nickname }}</b>
          &nbsp;
          <span v-if="message.type == 'comment'">评论了你</span>
          <span v-if="message.type == 'reply'">回复了你</span>
          <span v-if="message.type == 'post'">发布了</span>
        </div>
        <div class="ma-1 text-caption">
          {{ message.text }}
        </div>
      </v-col>
      <v-spacer></v-spacer>
      <v-btn
        :class="{ 'show-btn': hover }"
        :color="transparent"
        icon
        style="position: fixed"
        @click="read()"
      >
        <v-icon
          v-if="!message.read"
          :class="{ 'show-btn': hover }"
          :color="transparent"
        >
          mdi-eye-off
        </v-icon>
        <v-icon v-else :class="{ 'show-btn': hover }" :color="transparent">
          mdi-eye
        </v-icon>
      </v-btn>
      <v-spacer></v-spacer>
      <div class="ma-4 text-caption">
        {{ message.time }}
      </div>
    </v-card>
  </v-hover>
</template>

<script>
export default {
  props: {
    message: {
      type: Object,
    },
  },
  data() {
    return {
      dark: this.$vuetify.theme.dark,
      transparent: "rgba(255,255,255,0)",
    };
  },
  methods: {
    read() {
      this.message.read = !this.message.read;
    },
  },
};
</script>

<style>
.show-btn {
  color: rgb(96, 96, 96) !important;
}
</style>
