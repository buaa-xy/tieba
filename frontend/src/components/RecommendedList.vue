<template>
  <v-list style="position: fixed">
    <user-card
      v-for="user in users.slice(0, 5)"
      :key="user.username"
      :user="user"
    ></user-card>
  </v-list>
</template>

<script>
import UserCard from "@/components/cards/UserCard.vue";

export default {
  components: {
    "user-card": UserCard,
  },
  props: ["users"],
};
</script>

<style></style>
