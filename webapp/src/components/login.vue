<template>
  <v-card width="420px" :elevation="6" :loading="loading">
    <v-form @submit.prevent="login">
      <v-img src="../assets/astromaute.svg" height="150px"> </v-img>
      <v-card-title class="justify-center">Connexion</v-card-title>
      <v-card-text>
        <v-text-field
          label="Identifiant"
          v-model="username"
          prepend-icon="person"
          type="text"
        />

        <v-text-field
          label="Mot de passe"
          v-model="password"
          prepend-icon="lock"
          type="current-password"
        />
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <router-link to="/auth/register">Nouveau ? S'enregistrer</router-link>

        <v-btn color="primary" type="submit">Se connecter</v-btn>
      </v-card-actions>
    </v-form>
    <v-expand-transition>
      <div v-show="show" class="red">
        <v-divider></v-divider>

        <v-card-text>
          Identifiant ou mot de passe incorrect.
        </v-card-text>
      </div>
    </v-expand-transition>
  </v-card>
</template>

<script>
import { AUTH_REQUEST } from "../store/actions";

export default {
  data: () => {
    return {
      username: "",
      password: ""
    };
  },
  methods: {
    login: function() {
      const { username, password } = this;
      this.$store.dispatch(AUTH_REQUEST, { username, password }).then(() => {
        this.$router.push("/");
      });
    }
  },
  computed: {
    show: function() {
      return this.$store.getters.authStatus == "error";
    },
    loading: function() {
      return this.$store.getters.authStatus == "loading";
    }
  }
};
</script>

<style scoped></style>
