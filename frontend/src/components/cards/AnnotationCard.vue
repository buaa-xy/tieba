<template>
  <div class="annotate">
    <v-card class="card" outlined height="100%" width="400">
      <!-- Banner -->
      <v-alert border="top" color="cyan">
        <v-row align="center" no-gutters>
          <v-col class="shrink">
            <v-icon>mdi-map-marker-outline</v-icon>
          </v-col>
          <v-col class="grow">
            <b> 批注 </b>
          </v-col>
          <v-spacer></v-spacer>
          <v-col class="shrink">
            <v-btn icon plain @click="$emit('getClose')">
              <v-icon>mdi-close</v-icon>
            </v-btn>
          </v-col>
        </v-row>
      </v-alert>
      <!-- Content -->
      <v-container class="overflow-auto" v-if="annotations.length != 0">
        <div
          class="mx-2"
          v-for="annotation in annotations"
          :key="annotation.annotationId"
        >
          <v-divider></v-divider>
          <div>
            <span>{{ annotation.nickname }}</span>
            <small v-if="annotation.replyToUserNickname" class="mx-1">回复</small>
            <small v-else> 批注 </small>
            <a
              v-if="annotation.replyToUserNickname"
              :href="'../user/' + annotation.replyToUserId"
            >@{{annotation.replyToUserNickname}}
            </a>
            <span>:{{ annotation.content }}</span>
          </div>
          <div>
            <small v-text="timeFormat(annotation.time)"></small>
            <v-btn plain small @click="toname = annotation.nickname, toId = annotation.annotationId">
              <v-icon small> mdi-message-text </v-icon>
              回复
            </v-btn>
          </div>
        </div>
      </v-container>
      <v-container v-else> 暂无批注 </v-container>
      <!-- Reply -->
      <v-container class="d-flex align-center mt-auto">
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
          :placeholder="toname ? '回复@' + toname + ':' : '批注:'"
          hide-details
          :prepend-inner-icon="toname && !text ? 'mdi-close-circle' : null"
          @click:prepend-inner="toname = null"
          append-icon="mdi-send-circle"
          @click:append="submit()"
          v-model="text"
        >
        </v-textarea>
      </v-container>
    </v-card>
  </div>
</template>

<script>
export default {
  props: ["annotations", "blockId"],
  data() {
    return {
      username: localStorage.getItem("username"),
      avatar: localStorage.getItem("avatar"),
      toname: null,
      focus: false,
      text: null,
    };
  },
  methods: {
    submit() {
      this.$axios({
        method: "post",
        url: "/api/annotate",
        data: {
          blockId: this.blockId,
          replyToId: this.toId,
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

<style scoped lang="scss">
.annotate {
  margin: 12px;
  height: calc(60% - 12px);
}
.card {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}
</style>
