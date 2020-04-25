<template>
  <v-card>
    <v-container>
      <v-form ref="form" v-model="valid">
        <v-text-field
          v-model="serie.title"
          :rules="[v => !!v || this.$t('series.nameRule')]"
          :label="$t('series.name') + '*'"
          prepend-icon="title"
          required
        ></v-text-field>

        <v-textarea
          v-model="serie.description"
          :rules="[v => !!v || this.$t('series.descriptionRule')]"
          :label="$t('series.description') + '*'"
          rows="4"
          prepend-icon="comment"
          required
        ></v-textarea>

        <v-select
          v-model="serie.sharedTo"
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
              <span> @{{ item.text }} </span>
              <v-icon small @click="parent.selectItem(item)">close</v-icon>
            </v-chip>
          </template>
          <template v-slot:item="{ index, item }">
            <v-chip label small> @{{ item.text }} </v-chip>

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
import { EventBus } from "../event-bus";
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
      var mwa = this.$store.getters.mwaMeme;
      service.getUsers().then(response => {
        this.users = response.data
          .filter(u => u.login != mwa.login)
          .map(u => {
            return { id: u.id, text: u.login, isReadOnly: true };
          });
      });
    },
    switchRO(item) {
      item.isReadOnly = !item.isReadOnly;
    },
    validate: function() {
      this.$refs.form.validate();
      if (this.valid) {
        this.$refs.form.resetValidation();
        this.$emit(this.editionMode ? "edit" : "add", this.serie);
      }
    },
    quit: function() {
      this.$refs.form.resetValidation();
      this.$emit("quit");
    }
  },
  mounted: function() {
    this.getUsers();
    EventBus.$on("langChange", () => {
      if (this.$refs.form != undefined) this.$refs.form.resetValidation();
    });
  },
  computed: {
    editionMode() {
      return !this.serie.empty;
    }
  }
};
</script>

<style scoped></style>
