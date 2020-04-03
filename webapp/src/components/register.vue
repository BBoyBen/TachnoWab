<template>
  <v-card width="420px" :elevation="6">
    <v-form>
      <v-img src="../assets/astromaute.svg" height="150px"></v-img>
      <v-card-title class="justify-center">{{
        $t("auth.register")
      }}</v-card-title>
      <v-card-text>
        <v-text-field
          :label="$t('auth.login')"
          name="login"
          prepend-icon="person"
          type="text"
        />

        <v-text-field
          id="password"
          :label="$t('auth.password')"
          name="password"
          prepend-icon="lock"
          type="password"
        />

        <v-text-field
          id="repassword"
          :label="$t('auth.repassword')"
          name="repassword"
          prepend-icon="lock"
          type="password"
        />
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <router-link to="/auth"
          >{{ $t("auth.alreadySignUp") }} {{ $t("auth.signIn") }}</router-link
        >

        <v-btn color="primary">{{ $t("auth.signUp") }}</v-btn>
      </v-card-actions>
    </v-form>
  </v-card>
</template>

<script>
export default {};
</script>

<style scoped></style>
