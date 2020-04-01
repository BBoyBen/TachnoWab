<template>
  <v-card width="420px" :elevation="6">
    <v-form>
      <v-img src="../assets/astromaute.svg" height="150px"> </v-img>
      <v-card-title class="justify-center">Enregistrement</v-card-title>
      <v-card-text>
        <v-text-field
          label="Identifiant"
          name="login"
          prepend-icon="person"
          type="text"
        />

        <v-text-field
          id="password"
          label="Mot de passe"
          name="password"
          prepend-icon="lock"
          type="password"
        />

        <v-text-field
          id="repassword"
          label="Confirmer le mot de passe"
          name="repassword"
          prepend-icon="lock"
          type="password"
        />
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <router-link to="/auth">Déjà inscrit ?</router-link>

        <v-btn color="primary">S'enregistrer</v-btn>
      </v-card-actions>
    </v-form>
  </v-card>
</template>

<script>
export default {};
</script>

<style scoped></style>
