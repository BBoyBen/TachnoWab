<template>
  <v-card class="mx-auto" max-width="80%" tile elevation="6">
    <v-card-title v-text="name" class="headline blue--text"></v-card-title>
    <v-card-text>
      <p class="text--primary">
        {{ description }}
      </p></v-card-text
    >

    <v-divider></v-divider>

    <v-container fill-height fluid class="spacing-playground px-6 py-6">
      <v-row align="center" justify="center" class="px-3">
        <v-text-field
          v-model="filterTags"
          prepend-icon="search"
          :label="$t('series.event.filter')"
          :hint="$t('series.event.hint')"
        />
      </v-row>
      <v-row align="center" justify="center">
        <v-col cols="12" sm="6" md="4">
          <v-menu
            v-model="sDatemenu"
            :close-on-content-click="false"
            :nudge-right="40"
            transition="scale-transition"
            offset-y
            min-width="290px"
          >
            <template v-slot:activator="{ on }">
              <v-text-field
                v-model="sDate"
                :label="$t('series.event.startDate')"
                prepend-icon="event"
                clearable
                v-on="on"
              ></v-text-field>
            </template>
            <v-date-picker
              v-model="sDate"
              @input="sDatemenu = false"
            ></v-date-picker>
          </v-menu>
        </v-col>
        <v-col cols="12" sm="6" md="4">
          <v-menu
            v-model="eDatemenu"
            :close-on-content-click="false"
            :nudge-right="40"
            transition="scale-transition"
            offset-y
            min-width="290px"
          >
            <template v-slot:activator="{ on }">
              <v-text-field
                v-model="eDate"
                :label="$t('series.event.endDate')"
                prepend-icon="event"
                clearable
                v-on="on"
              ></v-text-field>
            </template>
            <v-date-picker
              v-model="eDate"
              @input="eDatemenu = false"
            ></v-date-picker>
          </v-menu>
        </v-col>
        <v-col cols="12" sm="6" md="4">
          <v-btn
            :loading="search"
            :disabled="!btnSearch"
            @click="loader = 'search'"
          >
            <v-icon left>mdi-magnify</v-icon>
            {{ $t("common.search") }}
          </v-btn>
        </v-col>
      </v-row>
    </v-container>

    <v-divider></v-divider>

    <v-container>
      <v-row align="center" justify="center">
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <v-btn v-on="on" icon @click="showGraph = !showGraph">
              <v-icon>{{
                showGraph ? "mdi-chevron-up" : "mdi-chevron-down"
              }}</v-icon>
            </v-btn>
          </template>
          <span>{{
            $t("series.event.graph", {
              status: $t(`common.${!showGraph ? "show" : "hide"}`)
            })
          }}</span>
        </v-tooltip>
      </v-row>
      <v-sparkline
        v-if="showGraph"
        :labels="labels"
        :value="values"
        color="white"
        line-width="1"
        padding="20"
      >
        <template v-slot:label="item">
          {{
            item.index == 0 || item.index == filtered.length - 1
              ? item.value
              : ""
          }}
        </template>
      </v-sparkline>
    </v-container>
    <v-data-table
      v-model="selected"
      :headers="headers"
      :items="events"
      :single-select="false"
      show-select
      class="elevation-0 mx-0 my-6"
      dense
      @current-items="getFiltered"
    >
      <template v-slot:top>
        <v-row align="center" justify="center" class="mb-3">
          <v-btn v-if="selected.length != 0" color="red" @click="deleteEvents">
            <v-icon left>mdi-delete</v-icon>
            {{ $t("common.delete") }}
          </v-btn>
        </v-row>
      </template>
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
    showGraph: false,
    filtered: [],
    serie: {},
    toAdd: {},
    events: [],
    selected: [],
    sDate: undefined,
    sDatemenu: false,
    eDate: undefined,
    eDatemenu: false,
    loader: null,
    filterTags: undefined,
    search: false
  }),
  methods: {
    getFiltered(events) {
      this.filtered = events;
    },
    deleteEvents() {
      this.selected.forEach(event => {
        service.deleteEvent(event).then(response => {
          if (response.status == 200) {
            this.events.splice(this.events.indexOf(event), 1);
          }
        });
      });
    }
  },
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
        {
          text: this.$t("series.event.tags"),
          value: "tags",
          filter: value => {
            if (!this.filterTags) return true;
            const fEvery = e => value.includes(e);

            return this.filterTags.split(";").every(fEvery);
          }
        }
      ];
    },
    labels() {
      return this.filtered.map(e => new Date(e.date).toLocaleDateString());
    },
    values() {
      return this.filtered.map(e => e.value);
    },
    name() {
      return `${this.$t("series.name")} : ${this.serie.title}`;
    },
    description() {
      return `${this.$t("series.description")} : ${this.serie.description}`;
    },
    btnSearch() {
      return this.sDate != undefined && this.eDate != undefined;
    }
  },
  watch: {
    loader() {
      const l = this.loader;
      this[l] = !this[l];

      service
        .getEventsBySerieIdByDate(
          this.$route.params.id,
          new Date(this.sDate).toISOString(),
          new Date(this.eDate).toISOString()
        )
        .then(response => {
          this.events = (response.data || []).map(
            ev =>
              new EventSerie(ev.id, ev.date, ev.valeur, ev.commentaire, ev.tags)
          );
          this[l] = !this[l];
          this.loader = null;
        })
        .catch(() => {
          this[l] = !this[l];
          this.loader = null;
        });
    }
  }
};
</script>

<style scoped></style>
