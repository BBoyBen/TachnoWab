<template>
  <v-card class="mx-auto" max-width="80%" tile>
    <v-list three-line>
      <v-subheader>SERIES</v-subheader>
      <v-list-item v-for="item in items" :key="item.id">
        <v-list-item-avatar>
          <v-icon :class="[item.iconClass]" v-text="item.icon"></v-icon>
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
              <v-btn icon v-on="on">
                <v-icon color="red lighten-1">mdi-delete</v-icon>
              </v-btn>
            </template>
            <span>Supprimer</span>
          </v-tooltip>
        </v-list-item-action>
      </v-list-item>
    </v-list>
  </v-card>
</template>

<script>
// import axios from "axios";

export default {
  data: () => ({
    item: 0,
    items: null
  }),
  mounted() {
    // axios
    //   .get("/series")
    //   .then(response => (this.items = response.data));

    this.items = [{ id: "id", titre: "titre", description: "desc" }];
  }
};
</script>

<style scoped>
.routerLink {
  text-decoration: none;
}
</style>
