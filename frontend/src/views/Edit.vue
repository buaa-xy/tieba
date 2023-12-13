<template>
  <v-main>
    <navigation
      :flag="this.flag"
      :mode="this.mode"
      :change="this.disabled"
      @getMsg="update"
    ></navigation>
    <v-container fluid style="height: 100%">
      <v-alert
        v-model="alert"
        dismissible
        text
        outlined
        color="teal lighten-2"
        icon="mdi-file-edit-outline"
      >
        <pre v-if="mode == 'normal'">当前编辑器格式:<b>普通</b></pre>
        <pre v-else>当前编辑器格式:<b>markdown</b></pre>
      </v-alert>
      <v-text-field
        class="mb-4"
        v-model="title"
        :rules="titleRules"
        placeholder="标题......"
        hide-details="true"
        color="teal"
        outlined
      >
        <template v-slot:append>
          <pre>{{ title.length }}/50</pre>
        </template>
      </v-text-field>
      <mavon-editor
        class="editor"
        v-if="mode == 'markdown'"
        v-model="content1"
        ref="md"
        @imgAdd="$imgAdd"
      />
      <editor
        v-if="mode == 'normal'"
        v-model="content2"
        api-key="jgy2j8urc0kh9vnt1uj3euyb9w8uunl6cvrrw0oh5430vnd7"
        :init="editorConfig"
      />
      <v-overlay :value="overlay">
        <v-progress-circular
          v-if="loading"
          indeterminate
          size="64"
        ></v-progress-circular>
        <v-container fluid v-if="count > 0">
          <span class="d-flex align-center">
            <v-icon x-large>{{
              this.success ? "mdi-check-circle" : "mdi-close-circle"
            }}</v-icon>
            <span class="text-h5"
              >发布{{ this.success ? "成功" : "失败" }}</span
            >
          </span>
          <pre>将在{{ count }}秒后返回主页</pre>
          <v-divider></v-divider>
          <v-btn text @click="clearSeconds(false)">取消</v-btn>
          <v-btn plain color="teal" @click="clearSeconds(true)">立即返回</v-btn>
        </v-container>
      </v-overlay>
    </v-container>
  </v-main>
</template>

<script>
import composeNavigation from "@/components/navigation/ComposeNavigation.vue";
// import MarkdownEditor from "@/components/MarkdownEditor.vue";
import Editor from "@tinymce/tinymce-vue";

export default {
  components: {
    navigation: composeNavigation,
    editor: Editor,
    // markdown: MarkdownEditor,
  },
  data() {
    return {
      pid: null,
      title: "",
      content1: "",
      content2: "",
      flag: false,
      alert: true,
      mode: "normal",
      overlay: false,
      loading: false,
      success: false,
      seconds: null,
      disabled: true,
      count: 0,
      titleRules: [(v) => v.length <= 50 || "标题小于50字"],
      editorConfig: {
        height: 550,
        placeholder: "点击开始编辑......",
        plugins: [
          "lists",
          "link",
          "image",
          "paste",
          "help",
          "wordcount",
          "codesample",
          "preview",
          "textpattern",
          "fullscreen",
          "searchreplace",
        ],
        language: "zh_CN",
        skin: this.$vuetify.theme.dark ? "oxide-dark" : "oxide",
        content_css: this.$vuetify.theme.dark ? "dark" : "",
        toolbar: [
          "undo redo | bold italic underline | h1 h2 h3 h4 | numlist bullist | image codesample  searchreplace | fullscreen preview ",
        ],
        images_upload_handler: (blobInfo, success, failure) => {
          this.upload(blobInfo, success, failure);
        },
      },
    };
  },
  watch: {
    content() {
      this.flag = true;
    },
  },
  mounted() {
    this.pid = this.$route.params.pid;
    this.getData();
  },
  methods: {
    getData() {
      this.$axios({
        method: "get",
        url: "/api/post",
        params: {
          postId: this.pid,
        },
      })
        .then((response) => {
          if (response.data.success === true) {
            let data = response.data.data;
            this.title = data.post.title;
            if (data.post.annotationAllowed) this.mode = "markdown";
            this.getContent();
          } else {
            console.log(response);
            console.log(response.data);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    getContent() {
      this.$axios({
        method: "get",
        url: "/api/post/content",
        params: {
          postId: this.pid,
        },
      })
        .then((response) => {
          if (response.data.success === true) {
            if (this.mode == "markdown") this.content1 = response.data.data
            else this.content2 = response.data.data;
          } else {
            console.log(response);
            console.log(response.data);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    update(data) {
      switch (data) {
        case "markdown":
        case "normal":
          this.mode = data;
          break;
        case "submit":
          this.loading = true;
          this.overlay = true;
          this.submit();
          break;
        default:
          break;
      }
    },
    timeout(success) {
      this.success = success;
      this.loading = false;
      this.count = 5;
      this.seconds = setInterval(() => {
        this.count--;
        if (this.count <= 0) {
          clearInterval(this.seconds);
          this.overlay = false;
          this.$router.push("/home");
        }
      }, 1000);
    },
    clearSeconds(push) {
      if (this.seconds) {
        clearInterval(this.seconds);
        push ? this.$router.push("/home") : (this.overlay = false);
      }
    },
    submit() {
      let content = "";
      if (this.mode == "markdown") content = this.content1;
      else content = this.content2;
      this.$axios({
        method: "post",
        url: "/api/modify/post",
        data: {
          title: this.title,
          content: content,
          postId: this.pid,
        },
      })
        .then((response) => {
          if (response.data.success == true) {
            this.timeout(true);
          } else {
            this.timeout(false);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    upload(blobInfo, success, failure) {
      let formData = new FormData();
      formData.append("image", blobInfo.blob());
      this.$axios({
        method: "post",
        url: "/api/img/upload",
        data: formData,
      })
        .then((response) => {
          if (response.data.success == true) {
            let data = response.data.data;
            success("/api/img/" + data);
          } else {
            failure(response.data.message);
          }
        })
        .catch((error) => {
          failure(error);
        });
    },
    $imgAdd(pos, $file) {
      let formData = new FormData();
      formData.append("image", $file);
      this.$axios({
        method: "post",
        url: "/api/img/upload",
        data: formData,
      })
        .then((response) => {
          if (response.data.success == true) {
            let data = response.data.data;
            this.$refs.md.$img2Url(pos, "/api/img/" + data);
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

<style scoped>
.editor {
  z-index: 0 !important;
  font-family: "Fira code";
  height: 550px;
}
</style>
