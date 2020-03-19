<template>
  <v-app>
    <v-navigation-drawer
      color="light-blue accent-3"
      dark
      clipped
      fixed
      app
      v-model="drawer"
    >
      <v-list dense class="py-0">
        <router-link
          class="routerLink"
          v-for="item in items"
          :key="item.title"
          :to="item.link"
        >
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
          <v-btn block>Logout</v-btn>
        </div>
      </template>
    </v-navigation-drawer>

    <v-app-bar app color="light-blue accent-4" dark clipped-left>
      <v-btn icon color="white" @click.stop="drawer = !drawer">
        <v-icon>mdi-menu</v-icon>
      </v-btn>
      <v-toolbar-title>Application</v-toolbar-title>

      <v-spacer></v-spacer>

      <v-btn href="https://github.com/vuetifyjs/vuetify/releases/latest" text>
        <span class="mr-2">En/fr</span>
      </v-btn>
    </v-app-bar>

    <v-content>
      <v-container>
        <router-view />
      </v-container>
    </v-content>
  </v-app>
</template>

<script>
export default {
  data: () => ({
    drawer: undefined,
    items: [
      { title: "Acceuil", icon: "mdi-image", link: "/" },
      { title: "Series", icon: "mdi-view-dashboard", link: "/series" },
      { title: "About", icon: "mdi-help-box", link: "/" }
    ]
  })
};
</script>

<style scoped>
.routerLink {
  text-decoration: none;
}
</style>
