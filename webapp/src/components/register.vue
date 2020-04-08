<template>
  <v-form ref="form" @submit.prevent="signup" v-model="valid">
    <v-card-title class="justify-center">{{
      $t("auth.register")
    }}</v-card-title>
    <v-card-text>
      <v-text-field
        autocomplete="username"
        :label="$t('auth.login')"
        v-model="login"
        :rules="[v => !!v || this.$t('auth.loginRule')]"
        prepend-icon="person"
        type="text"
      />

      <v-text-field
        autocomplete="name"
        :label="$t('auth.name')"
        v-model="name"
        :rules="[v => !!v || this.$t('auth.nameRule')]"
        prepend-icon="person"
        type="text"
      />

      <v-text-field
        autocomplete="firstName"
        :label="$t('auth.firstName')"
        v-model="firstName"
        :rules="[v => !!v || this.$t('auth.firstNameRule')]"
        prepend-icon="person"
        type="text"
      />

      <v-text-field
        autocomplete="new-password"
        :label="$t('auth.password')"
        v-model="password"
        :rules="passwordRule"
        prepend-icon="lock"
        :append-icon="hidden ? 'mdi-eye' : 'mdi-eye-off'"
        @click:append="hidden = !hidden"
        :type="hidden ? 'password' : 'text'"
      />

      <v-text-field
        autocomplete="new-password"
        :label="$t('auth.repassword')"
        v-model="repassword"
        :rules="[v => v == password || this.$t('auth.repasswordRule')]"
        prepend-icon="lock"
        :append-icon="hidden ? 'mdi-eye' : 'mdi-eye-off'"
        @click:append="hidden = !hidden"
        :type="hidden ? 'password' : 'text'"
      />
    </v-card-text>
    <v-card-actions>
      <v-spacer />
      <router-link to="/auth"
        >{{ $t("auth.alreadySignUp") }} {{ $t("auth.signIn") }}</router-link
      >

      <v-btn color="primary" :disabled="!valid" @click="signup">{{
        $t("auth.signUp")
      }}</v-btn>
    </v-card-actions>
  </v-form>
</template>

<script>
import { EventBus } from "../event-bus";
import service from "../services/users";
import { AUTH_REQUEST } from "../store/actions";
import { UserToPost } from "../models/User";

export default {
  data: () => {
    return {
      valid: true,
      hidden: true,
      login: "admin",
      name: "Arnould",
      firstName: "Alexis",
      password: "1234567H",
      repassword: "1234567H"
    };
  },
  methods: {
    signup: function() {
      this.$refs.form.validate();
      if (this.valid) {
        const { login, name, firstName, password } = this;

        service
          .postUser(new UserToPost(name, firstName, login, password))
          .then(data => {
            if (data) {
              this.$store
                .dispatch(AUTH_REQUEST, { login, password })
                .then(() => {
                  this.$router.push("/");
                });
            }
          });
      }
    }
  },
  mounted: function() {
    EventBus.$on("langChange", () => {
      if (this.$refs.form != undefined) this.$refs.form.resetValidation();
    });
  },
  computed: {
    passwordRule() {
      return [
        v => {
          const pattern = /^(?=.*[A-Z])(?=.*[0-9])(?=.{8,})/;
          return pattern.test(v) || this.$t("auth.passwordRule");
        }
      ];
    }
  }
};
</script>

<style scoped></style>
