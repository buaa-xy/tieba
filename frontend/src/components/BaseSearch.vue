<template>
  <v-sheet class="scrollTop pa-4 d-flex align-center">
    <v-text-field
      style="width: 500px"
      class="mx-auto"
      color="teal"
      outlined
      hide-details="true"
      v-model="text"
      label="搜索Mark++"
      clear-icon="mdi-close-circle"
      clearable
      append-icon="mdi-magnify"
      @click:clear="clearText"
      @click:append="searchText"
      @keydown.enter="searchText"
    ></v-text-field>
    <v-btn text>
      最热
    </v-btn>
    <v-divider vertical></v-divider>
    <v-btn text>
      <b>最新</b>
    </v-btn>
  </v-sheet>
</template>

<script>
export default {
  data() {
    return {
      text: "",
    };
  },
  methods: {
    clearText() {
      this.text = "";
    },
    searchText() {
      if (this.text === "") {
        this.$notify({
          title: "提示",
          message: "请输入内容进行搜索",
          type: "warning",
        });
      } else {
        this.$axios({
          method: "get",
          url: "/api/search",
          params: {
            keyword: this.text,
          },
        })
          .then((response) => {
            if (response.data.success == true) {
              console.log(response.data.data);
              this.$emit("searchResult", response.data.data);
              this.text = "";
            } else {
              console.log(response);
              console.log(response.data.message);
            }
          })
          .catch((error) => {
            console.log(error);
          });
      }

      console.log(this.text);
    },
  },
};
</script>

<style scoped>
.scrollTop {
  position: sticky;
  top: 0;
  z-index: 10;
}
</style>
