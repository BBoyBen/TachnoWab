<template>
  <v-card>
    <v-card-title v-text="name" class="headline"></v-card-title>
    <v-card-text>
      <p class="text--primary">
        {{ description }}
      </p></v-card-text
    >

    <v-data-table
      v-model="selected"
      :headers="headers"
      :items="events"
      :single-select="true"
      show-select
      class="elevation-1"
      dense
    >
      <template v-slot:item.date="{ item }">
        <span>{{ new Date(item.date).toLocaleString() }}</span>
      </template>
    </v-data-table>
  </v-card>
</template>

<script>
import service from "../services/series";
import { Serie } from "../models/Serie";
import { EventSerie } from "../models/EventSerie";
import Vue from "vue";

export default {
  data: () => ({
    serie: {},
    toAdd: {},
    events: [],
    selected: []
  }),
  mounted() {
    service
      .getSerieById(this.$route.params.id)
      .then(response => {
        if (response.status == 200) {
          this.serie = new Serie(
            response.data.id,
            response.data.titre,
            response.data.description,
            []
          );
        }
      })
      .catch(() => {
        this.$router.push("/series");
        Vue.toasted.error(this.$t(`error.access.serie`), {
          icon: "error_outline",
          position: "bottom-right",
          duration: 4200,
          action: {
            icon: "close",
            onClick: (e, toastObject) => {
              toastObject.goAway(0);
            }
          }
        });
      });
    service.getEventsBySerieId(this.$route.params.id).then(response => {
      if (response.status == 200) {
        response.data.forEach(ev => {
          this.events.push(
            new EventSerie(ev.id, ev.date, ev.valeur, ev.commentaire, ev.tags)
          );
        });
      }
    });
  },
  computed: {
    headers() {
      return [
        { text: this.$t("series.event.date"), value: "date" },
        { text: this.$t("series.event.value"), value: "value" },
        { text: this.$t("series.event.comment"), value: "comment" },
        { text: this.$t("series.event.tags"), value: "tags" }
      ];
    },
    name() {
      return `${this.$t("series.name")} : ${this.serie.title}`;
    },
    description() {
      return `${this.$t("series.description")} : ${this.serie.description}`;
    }
  }
};
</script>

<style scoped></style>
