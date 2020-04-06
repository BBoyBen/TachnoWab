<template>
  <v-container fluid>
    <v-row>
      <v-col>
        <v-row align="center" justify="center">
          <v-card width="420px" :elevation="6" :loading="loading">
            <v-img src="../assets/astromaute.svg" height="150px"></v-img>
            <router-view />
          </v-card>
        </v-row>
      </v-col>
    </v-row>
    <lang-selector></lang-selector>
  </v-container>
</template>

<script>
import langSelector from "../components/langSelector";

export default {
  components: {
    langSelector
  },
  computed: {
    loading: function() {
      return this.$store.getters.authStatus == "loading";
    }
  }
};
</script>

<style scoped></style>
