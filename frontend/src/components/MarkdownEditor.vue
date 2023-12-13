<template>
  <div>
    <!-- 编辑器按钮 -->
    <v-btn-toggle class="my-2">
      <v-btn @click="applyBold">
        <v-icon>mdi-format-bold</v-icon>
      </v-btn>
      <v-btn @click="applyItalic">
        <v-icon>mdi-format-italic</v-icon>
      </v-btn>
      <v-btn @click="applyHeading">
        <v-icon>mdi-alpha-h-box</v-icon>
      </v-btn>
      <v-btn @click="applyList1">
        <v-icon>mdi-format-list-bulleted</v-icon>
      </v-btn>
      <v-btn @click="applyList2">
        <v-icon>mdi-format-list-numbered</v-icon>
      </v-btn>
      <v-btn @click="undo">
        <v-icon>mdi-undo-variant</v-icon>
      </v-btn>
      <v-btn @click="redo">
        <v-icon>mdi-redo-variant</v-icon>
      </v-btn>
    </v-btn-toggle>
    <!-- 文本 -->
    <div
      id="md-textarea"
      @input="onInput"
      contenteditable="true"
      v-html="innerValue"
    ></div>
  </div>
</template>

<script>
import { Marked } from "@ts-stack/markdown";
import TurndownService from "turndown";

export default {
  name: "MarkdownEditor",
  props: ["value"],
  data() {
    return {
      innerValue: Marked.parse(this.value) || "<p><br></p>",
    };
  },
  mounted() {
    const keydown = document.getElementById("md-textarea");
    keydown.addEventListener("keydown", (event) => {
      if (event.isComposing || event.key === "Enter") {
        const turndown = new TurndownService({
          emDelimiter: "_",
          linkStyle: "inlined",
          headingStyle: "atx",
        });
        //alert(format_markdown)
        this.innerValue = Marked.parse(turndown.turndown(this.innerValue));
      }
    });
    // document.execCommand("defaultParagraphSeparator", false, "p")
  },
  methods: {
    onInput(event) {
      const turndown = new TurndownService({
        emDelimiter: "_",
        linkStyle: "inlined",
        headingStyle: "atx",
      });
      this.$emit("input", turndown.turndown(event.target.innerHTML));
    },
    applyBold() {
      document.execCommand("bold");
    },
    applyItalic() {
      document.execCommand("italic");
    },
    applyHeading() {
      console.log("heading");
    },
    applyList1() {
      document.execCommand("insertUnorderedList");
    },
    applyList2() {
      document.execCommand("insertOrderedList");
    },
    undo() {
      document.execCommand("undo");
    },
    redo() {
      document.execCommand("redo");
    },
  },
};
</script>

<style scoped>
#md-textarea {
  font-family: "Fira code";
  padding: 10px 10px 10px 10px;
  border: 2px solid;
}
</style>
