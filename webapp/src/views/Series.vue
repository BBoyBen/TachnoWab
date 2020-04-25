<template>
  <div>
    <v-card class="mx-auto" max-width="80%" tile elevation="6">
      <v-card-title>
        <v-card-title>{{ $t("series.title") }}</v-card-title>
        <v-spacer></v-spacer>
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <v-btn v-on="on" @click="dialog = true" icon class="mr-4">
              <v-icon>mdi-plus</v-icon>
            </v-btn>
          </template>
          <span>{{ $t("series.add") }}</span>
        </v-tooltip>
      </v-card-title>
      <v-list>
        <serie
          :serie="item"
          :index="index"
          v-for="(item, index) in items"
          :key="item.id"
          @edit="startEdit"
          @delete="deleteSerie"
        />
      </v-list>
    </v-card>

    <v-dialog v-model="dialog" persistent max-width="42%">
      <v-container>
        <serieForm
          :serie="selected"
          @add="addSerie"
          @edit="editSerie"
          @quit="closeModal"
        />
      </v-container>
    </v-dialog>
  </div>
</template>

<script>
import seriesService from "../services/series";
import sharingService from "../services/sharing";
import serieForm from "../components/serieForm";
import serie from "../components/serie";
import { empty, Serie } from "../models/Serie";
import { Share } from "../models/Share";

export default {
  data: () => ({
    selectedIndex: null,
    selected: null,
    items: [],
    dialog: false
  }),
  components: {
    serieForm,
    serie
  },
  methods: {
    addSerie(value) {
      seriesService.postSerie(value).then(response => {
        if (response) {
          var serie = response.data;
          value.id = serie.id;
          sharingService.postSharesForSerie(value);
          this.items.push(
            new Serie(serie.id, serie.titre, serie.description, value.sharedTo)
          );
          this.closeModal();
        }
      });
    },
    startEdit(index) {
      this.selectedIndex = index;
      this.selected = this.items[index].copy();
      this.dialog = true;
    },
    editSerieSharing(oldOne, newOne) {
      // WIP
      // foreach oldOne.sharedTo new => postSharing, existing => putSharing, old => deleteSharing
      var news = [];
      var olds = [];
      var updates = [];
      newOne.sharedTo.forEach(s => {
        var found = oldOne.sharedTo.find(e => s.text == e.text);
        if (found) {
          if (s.isReadOnly != found.isReadOnly) updates.push(s);
          else olds.push(s);
        } else news.push(s);
      });
    },
    editSerie(value) {
      seriesService.putSerie(value).then(response => {
        if (response.status == 200) {
          var oldOne = this.items[this.selectedIndex];
          var newOne = value;
          this.items[this.selectedIndex] = new Serie(
            response.data.id,
            response.data.titre,
            response.data.description,
            newOne.sharedTo
          );
          this.editSerieSharing(oldOne, newOne);
          this.closeModal();
        }
      });
    },
    deleteSerie(index) {
      seriesService.deleteSerie(this.items[index]).then(response => {
        if (response.status == 200) this.items.splice(index, 1);
        // foreach serie.sharedTo => deleteSharing
      });
    },
    async getSeries() {
      const response = await seriesService.getSeries();
      (response.data || []).map(serie => {
        var sharedTo = [];
        sharingService.getSharesForSerie(serie.id).then(response => {
          if (response.data) {
            response.data.forEach(s => {
              sharedTo.push(
                new Share(s.idUtilisateur, s.loginUtilisateur, s.lectureSeule)
              );
            });
          }
          this.items.push(
            new Serie(serie.id, serie.titre, serie.description, sharedTo)
          );
        });
      });
    },
    async getShares() {
      const response = await sharingService.getShares();
      (response.data || []).map(share => {
        var sharedTo = [];
        sharingService.getSharesForSerie(share.idSerie).then(response => {
          if (response.data) {
            response.data.forEach(s => {
              sharedTo.push(
                new Share(s.idUtilisateur, s.loginUtilisateur, s.lectureSeule)
              );
            });
          }
          this.items.push(
            new Serie(
              share.idSerie,
              share.titre,
              share.description,
              sharedTo,
              share.lectureSeule
            )
          );
        });
      });
    },
    closeModal() {
      this.dialog = false;
      this.selected = empty();
    }
  },
  mounted() {
    this.selected = empty();
    this.getSeries();
    this.getShares();
  }
};
</script>

<style scoped></style>
