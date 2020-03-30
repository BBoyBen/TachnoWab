<template>
  <div>
    <v-navigation-drawer clipped fixed app v-model="drawer">
      <v-list>
        <v-list-item>
          <v-list-item-avatar height="63" width="63">
            <v-img src="../assets/logo.svg"></v-img>
          </v-list-item-avatar>
        </v-list-item>

        <v-list-item>
          <v-list-item-content>
            <v-list-item-title class="title">John Leider</v-list-item-title>
            <v-list-item-subtitle>john@vuetifyjs.com</v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
      </v-list>
      <v-divider></v-divider>
      <v-list dense class="py-0">
        <router-link v-for="item in items" :key="item.title" :to="item.link">
          <v-list-item link>
            <v-list-item-icon>
              <v-icon>{{ item.icon }}</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title>{{ item.title }}</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </router-link>
      </v-list>

      <template v-slot:append>
        <div class="pa-2">
          <v-btn block @click="logout">Déconnexion</v-btn>
        </div>
      </template>
    </v-navigation-drawer>

    <v-app-bar app clipped-left>
      <v-btn icon @click.stop="drawer = !drawer">
        <v-icon>mdi-menu</v-icon>
      </v-btn>
      <v-toolbar-title>Application</v-toolbar-title>

      <v-spacer></v-spacer>

      <v-btn href="http://www.originskrew.com/" text>
        <span class="mr-2">En/fr</span>
      </v-btn>
    </v-app-bar>

    <v-content>
      <v-container>
        <router-view />
      </v-container>
    </v-content>
  </div>
</template>

<script>
import { AUTH_LOGOUT } from "../store/actions";

export default {
  data: () => ({
    drawer: undefined,
    items: [
      { title: "Acceuil", icon: "mdi-image", link: "/" },
      { title: "Series", icon: "mdi-view-dashboard", link: "/series" },
      { title: "À propos", icon: "mdi-help-box", link: "/about" }
    ]
  }),
  methods: {
    logout: function() {
      this.$store.dispatch(AUTH_LOGOUT).then(() => {
        this.$router.push("/auth");
      });
    }
  }
};
</script>
