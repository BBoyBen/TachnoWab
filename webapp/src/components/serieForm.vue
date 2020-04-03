<template>
  <v-card>
    <v-container>
      <v-form ref="form" v-model="valid">
        <v-text-field
          v-model="model.titre"
          :rules="[v => !!v || this.$t('series.nameRule')]"
          :label="$t('series.name') + '*'"
          prepend-icon="title"
          required
        ></v-text-field>

        <v-textarea
          v-model="model.description"
          :rules="[v => !!v || this.$t('series.descriptionRule')]"
          :label="$t('series.description') + '*'"
          rows="4"
          prepend-icon="comment"
          required
        ></v-textarea>

        <v-select
          v-model="model.sharedTo"
          :items="users"
          :label="$t('series.sharedTo')"
          prepend-icon="account_circle"
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
                  $t(
                    item.isReadOnly
                      ? "series.editionForbid"
                      : "series.editionGrant"
                  )
                }}</span>
              </v-tooltip>
            </v-list-item-action>
          </template>
        </v-select>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="error" class="mr-4" @click="quit">
            {{ $t("common.cancel") }}
          </v-btn>
          <v-btn
            color="success"
            class="mr-4"
            :disabled="!valid"
            @click="validate"
          >
            {{ $t(editionMode ? "common.edit" : "common.add") }}
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
      firstValid: true,
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
      this.$refs.form.resetValidation();
      if (!this.editionMode) this.$refs.form.reset();
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
