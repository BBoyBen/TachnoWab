<template>
  <v-card>
    <v-container>
      <v-form ref="form" v-model="valid">
        <v-text-field
          v-model="model.titre"
          :rules="[v => !!v || 'Le nom est obligatoire']"
          label="Nom *"
          required
        ></v-text-field>

        <v-text-field
          v-model="model.description"
          :rules="[v => !!v || 'La description est obligatoire']"
          label="Description *"
          required
        ></v-text-field>

        <v-select
          v-model="model.sharedTo"
          :items="users"
          label="Partager à ..."
          :return-object="true"
          multiple
        >
          <template v-slot:selection="{ attrs, item, parent, selected }">
            <v-chip
              v-if="item === Object(item)"
              v-bind="attrs"
              :input-value="selected"
              label
              small
            >
              <span>
                {{ item.text }}
              </span>
              <v-icon small @click="parent.selectItem(item)">close</v-icon>
            </v-chip>
          </template>
          <template v-slot:item="{ index, item }">
            <v-chip label small>
              {{ item.text }}
            </v-chip>

            <v-spacer></v-spacer>

            <v-list-item-action @click.stop>
              <v-tooltip bottom>
                <template v-slot:activator="{ on }">
                  <v-btn v-on="on" icon @click.stop.prevent="switchRO(item)">
                    <v-icon>
                      {{ item.isReadOnly ? "mdi-eye" : "mdi-pencil" }}
                    </v-icon>
                  </v-btn>
                </template>
                <span>{{
                  item.isReadOnly ? "Édition interdite" : "Édition possible"
                }}</span>
              </v-tooltip>
            </v-list-item-action>
          </template>
        </v-select>

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
            {{ editionMode ? "Modifier" : "Ajouter" }}
          </v-btn>
        </v-card-actions>
      </v-form>
    </v-container>
  </v-card>
</template>

<script>
import service from "../services/users";

export default {
  props: {
    serie: {
      type: Object
    }
  },
  data: () => {
    return {
      valid: true,
      users: []
    };
  },
  methods: {
    async getUsers() {
      const response = service.getUsers();
      this.users = response.data;
    },
    switchRO(item) {
      item.isReadOnly = !item.isReadOnly;
    },
    validate: function() {
      this.$refs.form.validate();
      if (this.valid) {
        this.$refs.form.resetValidation();
        this.$emit(this.editionMode ? "edit" : "add", this.model);
      }
    },
    quit: function() {
      this.$emit("quit");
    }
  },
  mounted() {
    this.getUsers();
  },
  computed: {
    editionMode() {
      return this.serie;
    },
    model() {
      return (
        this.serie || {
          titre: "",
          description: "",
          sharedTo: []
        }
      );
    }
  }
};
</script>

<style scoped></style>
