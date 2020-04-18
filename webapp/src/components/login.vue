<template>
  <div>
    <v-form @submit.prevent="signin">
      <v-card-title class="justify-center">{{
        $t("auth.connection")
      }}</v-card-title>
      <v-card-text>
        <v-text-field
          autocomplete="username"
          :label="$t('auth.login')"
          v-model="username"
          prepend-icon="person"
          type="text"
        />

        <v-text-field
          autocomplete="current-password"
          :label="$t('auth.password')"
          v-model="password"
          prepend-icon="lock"
          :append-icon="hidden ? 'mdi-eye' : 'mdi-eye-off'"
          @click:append="hidden = !hidden"
          :type="hidden ? 'password' : 'text'"
        />
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <router-link to="/auth/register" style="margin-right: 10px;"
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
  </div>
</template>

<script>
import { AUTH_REQUEST } from "../store/actions";

export default {
  data: () => {
    return {
      hidden: true,
      username: "admin",
      password: "1234567H"
    };
  },
  methods: {
    signin: function() {
      const { username, password } = this;
      this.$store
        .dispatch(AUTH_REQUEST, { login: username, password })
        .then(() => {
          this.$router.push("/series");
        });
    }
  },
  computed: {
    show: function() {
      return this.$store.getters.authStatus == "error";
    }
  }
};
</script>

<style scoped></style>
