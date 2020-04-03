<template>
  <v-card width="420px" :elevation="6" :loading="loading">
    <v-form @submit.prevent="login">
      <v-img src="../assets/astromaute.svg" height="150px"> </v-img>
      <v-card-title class="justify-center">{{
        $t("auth.connection")
      }}</v-card-title>
      <v-card-text>
        <v-text-field
          :label="$t('auth.login')"
          v-model="username"
          prepend-icon="person"
          type="text"
        />

        <v-text-field
          :label="$t('auth.password')"
          v-model="password"
          prepend-icon="lock"
          type="current-password"
        />
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <router-link to="/auth/register"
          >{{ $t("auth.newSignUp") }} {{ $t("auth.signUp") }}</router-link
        >

        <v-btn color="primary" type="submit">{{ $t("auth.signIn") }}</v-btn>
      </v-card-actions>
    </v-form>
    <v-expand-transition>
      <div v-show="show" class="red">
        <v-divider></v-divider>

        <v-card-text>
          {{ $t("auth.badConnection") }}
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
      username: "admin",
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
