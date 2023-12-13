<template>
  <v-main>
    <home-navigation></home-navigation>
    <v-row no-gutters>
      <v-col cols="8">
        <notification-card
          v-for="(message, index) in messages"
          :key="index"
          :message="message"
        ></notification-card>
      </v-col>
      <v-col cols="4">
        <recommended-list></recommended-list>
      </v-col>
    </v-row>
  </v-main>
</template>

<script>
import NotificationCard from "@/components/cards/NotificationCard.vue";

export default {
  components: {
    "notification-card": NotificationCard,
  },
  data() {
    return {
    };
  },
  mounted() {
    this.getNotify();
  },
  methods: {
    getNotify() {
      this.$axios({
        method: "get",
        url: "/api/message",
        params: {},
      })
        .then((response) => {
          if (response.data.success == true) {
            let data = response.data.data;
            console.log("success");
            this.messages = data.messages;
          } else {
            console.log(response);
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

<style></style>
