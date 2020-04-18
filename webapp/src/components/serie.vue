<template>
  <v-card elevation="0">
    <v-list-item link three-line>
      <v-list-item-avatar>
        <!-- <v-icon v-text="item.icon"></v-icon> -->
        #{{ index + 1 }}
      </v-list-item-avatar>
      <v-list-item-avatar v-if="serie.isRO != undefined">
        <v-icon>
          {{ serie.isRO ? "mdi-eye" : "mdi-pencil" }}
        </v-icon>
      </v-list-item-avatar>

      <v-card width="85%" color="transparent" elevation="0">
        <router-link class="routerLink" :to="'/series/' + serie.id">
          <v-card-title v-text="serie.title" class="headline"></v-card-title>
          <v-card-text>
            <p class="text--primary">
              {{ serie.description }}
            </p></v-card-text
          >
        </router-link>
      </v-card>

      <v-list-item-action v-if="serie.isRO != true">
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <v-btn icon v-on="on" @click="$emit('edit', index)">
              <v-icon color="red lighten-1">mdi-pencil</v-icon>
            </v-btn>
          </template>
          <span>{{ $t("series.edit") }}</span>
        </v-tooltip>
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <v-btn icon v-on="on" @click="$emit('delete', index)">
              <v-icon color="red lighten-1">mdi-delete</v-icon>
            </v-btn>
          </template>
          <span>{{ $t("series.delete") }}</span>
        </v-tooltip>
        <v-btn icon @click="show = !show">
          <v-icon color="red lighten-1">{{
            show ? "mdi-chevron-up" : "mdi-chevron-down"
          }}</v-icon>
        </v-btn>
      </v-list-item-action>
    </v-list-item>
    <v-expand-transition>
      <v-form
        ref="form"
        v-model="valid"
        v-show="show"
        class="spacing-playground px-3 py-0"
      >
        <v-row>
          <v-col cols="2">
            <v-text-field
              v-model="value"
              :label="`${$t('series.event.value')} *`"
              :rules="[v => !!v || this.$t('series.event.valueRule')]"
              class="pr-1"
            ></v-text-field>
          </v-col>
          <v-col cols="4">
            <v-text-field
              v-model="comment"
              :label="$t('series.event.comment')"
              class="pr-1"
            ></v-text-field>
          </v-col>
          <v-col cols="5">
            <v-combobox
              v-model="tags"
              :label="$t('series.event.tags')"
              prepend-icon="mdi-tag"
              multiple
              deletable-chips
              small-chips
            ></v-combobox>
          </v-col>
          <v-col cols="1">
            <v-tooltip bottom>
              <template v-slot:activator="{ on }">
                <v-btn
                  icon
                  v-on="on"
                  @click="addEvent"
                  :disabled="!valid"
                  color="success"
                >
                  <v-icon>mdi-plus</v-icon>
                </v-btn>
              </template>
              <span>{{ $t("series.event.add") }}</span>
            </v-tooltip>
          </v-col>
        </v-row>
      </v-form>
    </v-expand-transition>
  </v-card>
</template>

<script>
import service from "../services/series";
import { EventSerie } from "../models/EventSerie";
import { EventBus } from "../event-bus";

export default {
  props: {
    serie: {
      type: Object
    },
    index: {}
  },
  data: () => {
    return {
      show: false,
      valid: false,
      value: undefined,
      comment: "",
      tags: []
    };
  },
  methods: {
    addEvent() {
      this.$refs.form.validate();
      if (this.valid) {
        service.postEvent(
          this.serie,
          new EventSerie("", "", this.value, this.comment, this.tags)
        );
        this.$refs.form.reset();
      }
    }
  },
  mounted: function() {
    EventBus.$on("langChange", () => {
      if (this.$refs.form != undefined) this.$refs.form.resetValidation();
    });
  }
};
</script>

<style scoped></style>
