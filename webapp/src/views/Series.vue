<template>
  <div>
    <v-card class="mx-auto" max-width="80%" tile>
      <v-card-title>
        <v-card-title>Séries</v-card-title>
        <v-spacer></v-spacer>
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <v-btn v-on="on" @click="dialog = true" icon class="mr-4">
              <v-icon>mdi-plus</v-icon>
            </v-btn>
          </template>
          <span>Ajouter un série</span>
        </v-tooltip>
      </v-card-title>
      <v-list three-line>
        <v-list-item v-for="(item, index) in items" :key="item.id" link>
          <v-list-item-avatar>
            <!-- <v-icon v-text="item.icon"></v-icon> -->
            {{ index }}
          </v-list-item-avatar>

          <v-list-item-content>
            <router-link class="routerLink" :to="'/series/' + item.id">
              <v-list-item-title v-text="item.titre"></v-list-item-title>
              <v-list-item-subtitle
                v-text="item.description"
              ></v-list-item-subtitle>
            </router-link>
          </v-list-item-content>

          <v-list-item-action>
            <v-tooltip bottom>
              <template v-slot:activator="{ on }">
                <v-btn
                  icon
                  v-on="on"
                  @click="
                    selected = item;
                    dialog = true;
                  "
                >
                  <v-icon color="red lighten-1">mdi-pencil</v-icon>
                </v-btn>
              </template>
              <span>Éditer</span>
            </v-tooltip>
            <v-tooltip bottom>
              <template v-slot:activator="{ on }">
                <v-btn icon v-on="on" @click="deleteSerie(index)">
                  <v-icon color="red lighten-1">mdi-delete</v-icon>
                </v-btn>
              </template>
              <span>Supprimer</span>
            </v-tooltip>
          </v-list-item-action>
        </v-list-item>
      </v-list>
    </v-card>

    <v-dialog v-model="dialog" persistent max-width="42%">
      <v-container>
        <serieForm
          :serie="selected"
          @add="addSerie"
          @edit="editSerie"
          @quit="
            dialog = false;
            item = null;
          "
        />
      </v-container>
    </v-dialog>
  </div>
</template>

<script>
import service from "../services/series";
import serieForm from "../components/serieForm";

export default {
  data: () => ({
    selected: null,
    items: [],
    dialog: false
  }),
  components: {
    serieForm
  },
  methods: {
    addSerie(value) {
      this.dialog = false;
      this.selected = null;

      this.items.push(service.postSerie(value));
    },
    editSerie(value) {
      this.dialog = false;
      this.selected = null;

      service.putSerie(value);
    },
    deleteSerie(index) {
      if (service.deleteSerie(this.items[index])) this.items.splice(index, 1);
    },
    async getSeries() {
      const response = service.getSeries();
      this.items = response.data;
    }
  },
  mounted() {
    this.getSeries();
  }
};
</script>

<style scoped>
.routerLink {
  text-decoration: none;
}
</style>