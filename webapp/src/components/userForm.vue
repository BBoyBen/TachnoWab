<template>
  <v-card class="mx-auto" max-width="63%" tile :elevation="6">
    <v-form ref="form" v-model="valid">
      <v-card-title class="justify-center">{{ $t("account.my") }}</v-card-title>
      <v-card-text>
        <v-text-field
          autocomplete="username"
          :label="$t('auth.login')"
          v-model="mwa.login"
          :rules="[v => !!v || this.$t('auth.loginRule')]"
          prepend-icon="person"
          type="text"
        />

        <v-text-field
          autocomplete="name"
          :label="$t('auth.name')"
          v-model="mwa.name"
          :rules="[v => !!v || this.$t('auth.nameRule')]"
          prepend-icon="person"
          type="text"
        />

        <v-text-field
          autocomplete="firstName"
          :label="$t('auth.firstName')"
          v-model="mwa.firstName"
          :rules="[v => !!v || this.$t('auth.firstNameRule')]"
          prepend-icon="person"
          type="text"
        />
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn color="success" :disabled="!valid" @click="edit">{{
          $t("common.edit")
        }}</v-btn>
        <v-spacer />
      </v-card-actions>
    </v-form>
  </v-card>
</template>

<script>
import service from "../services/users";
import { UserToPost } from "../models/User";
import { AUTH_SUCCESS } from "../store/actions";

export default {
  name: "user-form",
  data: () => {
    return {
      valid: true,
      mwa: {}
    };
  },
  mounted() {
    this.mwa = this.$store.getters.mwaMeme;
  },
  methods: {
    edit: function() {
      this.$refs.form.validate();
      if (this.valid) {
        service
          .putUser(
            new UserToPost(
              this.mwa.name,
              this.mwa.firstName,
              this.mwa.login,
              ""
            )
          )
          .then(response => {
            if (response.status == 200) {
              this.$toasted.success(this.$t("account.success"), {
                icon: "done",
                position: "bottom-right",
                duration: 4200,
                action: {
                  icon: "close",
                  onClick: (e, toastObject) => {
                    toastObject.goAway(0);
                  }
                }
              });
              this.$store.commit(AUTH_SUCCESS, this.mwa);
            }
          });
      }
    }
  }
};
</script>

<style scoped></style>
