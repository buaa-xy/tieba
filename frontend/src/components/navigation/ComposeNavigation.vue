<template>
  <v-navigation-drawer app class="text-center">
    <!-- 离开警告 -->
    <v-dialog v-model="dialog" width="500">
      <v-card>
        <v-card-title
          class="text-h5 teal"
          :class="this.$vuetify.theme.dark ? 'darken-4' : 'lighten-4'"
        >
          放弃编辑
        </v-card-title>
        <v-card-text class="my-5" style="font-size: 16px">
          <br />
          离开当前页面将导致已经编辑的内容丢失，且无法恢复。
          <br />
          <br />
          确认要放弃编辑，离开当前页面吗?
        </v-card-text>
        <v-divider></v-divider>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn text to="/home">确认离开</v-btn>
          <v-btn color="teal" outlined @click="dialog = false">继续编辑</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <div class="mt-4 mark-plus-plus">Mark++</div>
    <v-container class="px-8">
      <v-btn
        v-if="flag"
        class="text-h6 py-10"
        text
        block
        @click="dialog = true"
      >
        <v-icon> mdi-arrow-u-left-bottom-bold </v-icon>
        返回首页
      </v-btn>
      <v-btn v-else class="text-h6 py-10" text block to="/home">
        <v-icon> mdi-arrow-u-left-bottom-bold </v-icon>
        返回首页
      </v-btn>
      <v-btn v-if="!change" class="text-h6 py-10" text block @click="changeMode">
        <v-icon> mdi-language-markdown </v-icon>
        编辑模式
      </v-btn>
    </v-container>
    <template v-slot:append>
      <v-container fluid class="mb-8 px-8">
        <v-btn
          class="text-h6 py-9 my-9"
          color="teal"
          outlined
          block
          @click="submit"
        >
          <v-icon> mdi-send </v-icon>
          <b>发布内容</b>
        </v-btn>
        <user-avatar></user-avatar>
      </v-container>
    </template>
  </v-navigation-drawer>
</template>

<script>
import UserAvatar from "@/components/UserAvatar.vue";

export default {
  components: {
    "user-avatar": UserAvatar,
  },
  props: {
    flag: {
      type: Boolean,
      default: false,
    },
    mode: {
      type: String,
      default: "normal",
    },
    change: {
      type: Boolean,
      default: false,
    }
  },
  data() {
    return {
      dialog: false,
    };
  },
  methods: {
    changeMode() {
      if (this.mode == "markdown") {
        this.$emit("getMsg", "normal");
      } else {
        this.$emit("getMsg", "markdown");
      }
    },
    submit() {
      this.$emit("getMsg", "submit");
    },
  },
};
</script>

<style scoped>
.mark-plus-plus {
  font-family: "Courier New";
  font-size: 46px;
}
</style>
