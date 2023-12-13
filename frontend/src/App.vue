<template>
  <v-app>
    <router-view />
  </v-app>
</template>

<script>
export default {
  name: "App",
  data: () => ({
    //
  }),
};
</script>

<style lang="scss">
@import '@/assets/styles/styles.sass';
.v-application {
  font-family: $body-font-family, sans-serif !important;
  .title { // To pin point specific classes of some components
      font-family: $body-font-family, sans-serif !important;
  }
}
html,
body {
  height: 100%;
  margin: 0;
  overflow-y: auto !important;
  font-family: "Fira code";
}
:root {
  --glass-color: #ffffff70;
  --bg-color: #ffffff;
  --text-color: #777777;
  --heading-color: #484f58;
  --table-border-color: #111520;
  --table-header-bg-color: #1a1e29;
  --table-bg-color: var(--bg-color);
  --table-n2-bg-color: #a8a8a8;
}
</style>
