<template>
  <v-card>
    <v-container>
      <v-form ref="form" v-model="valid">
        <v-text-field
          v-model="titre"
          :rules="[v => !!v || 'Le nom est obligatoire']"
          label="Nom"
        ></v-text-field>
        <v-text-field
          v-model="description"
          :rules="[v => !!v || 'La description est obligatoire']"
          label="Description"
        ></v-text-field>
        <v-select
          v-model="sharedTo"
          :items="users"
          menu-props="auto"
          label="Shared to"
          prepend-icon="mdi-account-box"
          multiple
        ></v-select>
        <v-combobox
          multiple
          v-model="tags"
          label="Tags"
          append-icon
          chips
          deletable-chips
          class="tag-input"
        >
        </v-combobox>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="error" class="mr-4" @click="quit">
            Annuler
          </v-btn>
          <v-btn
            color="success"
            class="mr-4"
            :disabled="!valid"
            @click="validate"
          >
            Ajouter
          </v-btn>
        </v-card-actions>
      </v-form>
    </v-container>
  </v-card>
</template>

<script>
export default {
  data: () => {
    return {
      valid: true,
      titre: "",
      description: "",
      sharedTo: [],
      users: ["mwa-meme", "bénoit"],
      tags: []
    };
  },
  methods: {
    validate: function() {
      this.$refs.form.validate();
      if (this.valid)
        this.$emit("validate", {
          titre: this.titre,
          description: this.description,
          sharedTo: this.sharedTo,
          tags: this.tags
        });
    },
    quit: function() {
      this.$refs.form.reset();
      this.$emit("quit");
    }
  }
};
</script>

<style scoped></style>
