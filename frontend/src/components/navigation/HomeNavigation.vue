<template>
  <v-navigation-drawer app class="text-center">
    <div class="mt-4 mark-plus-plus">Mark++</div>
    <v-container class="px-8">
      <v-btn
        v-for="item in router"
        :key="item.title"
        :to="item.href"
        :color="item.color"
        class="text-h6 py-10"
        @click="item.block && block()"
        text
        block
      >
        <v-icon>
          {{ item.icon }}
        </v-icon>
        {{ item.title }}
      </v-btn>
    </v-container>
    <template v-slot:append>
      <v-container fluid class="mb-8 px-8">
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
  data() {
    return {
      router: [
        {
          href: "/home",
          title: "首页",
          icon: "mdi-book-open-blank-variant",
        },
        // { href: "/explore", title: "探索", icon: "mdi-earth" },
        {
          href: "/notifications",
          title: "通知",
          icon: "mdi-forum",
          block: true,
        },
        { href: "/follow", title: "关注", icon: "mdi-star-box", block: true },
        { href: "/settings", title: "设置", icon: "mdi-tune", block: true },
        {
          href: "/compose",
          title: "发帖",
          icon: "mdi-plus-box",
          block: true,
          color: "teal",
        },
      ],
    };
  },
  methods: {
    block() {
      let isLogin = sessionStorage.getItem("isLogin");
      if (!isLogin)
        this.$notify.warning({
          title: "请先登录",
          dangerouslyUseHTMLString: true,
          message: "没有账号？<br />点击“点击登录”按钮进行注册",
        });
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
