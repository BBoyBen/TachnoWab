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
            <v-list-item-title class="title"
              >{{ mwa.firstName }}
              {{ mwa.name | capitalize }}</v-list-item-title
            >
            <v-list-item-subtitle>{{ mwa.login }}</v-list-item-subtitle>
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
              <v-list-item-title>{{ $t(item.titleKey) }}</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </router-link>
      </v-list>

      <template v-slot:append>
        <div class="pa-2">
          <v-btn block @click="logout">{{ $t("auth.logout") }}</v-btn>
        </div>
      </template>
    </v-navigation-drawer>

    <v-app-bar app clipped-left>
      <div class="stars"></div>
      <div class="twinkling"></div>

      <v-btn icon @click.stop="drawer = !drawer">
        <v-icon>mdi-menu</v-icon>
      </v-btn>

      <v-toolbar-title class="title">{{
        $t("common.appname")
      }}</v-toolbar-title>

      <v-spacer></v-spacer>

      <div style="width: 150px; height: 50px">
        <v-select
          v-model="$i18n.locale"
          :items="langs"
          prepend-icon="language"
          solo
        >
          <template v-slot:selection="data">
            {{ $t(data.item.resKey) }}
          </template>
          <template v-slot:item="data">
            {{ $t(data.item.resKey) }}
          </template>
        </v-select>
      </div>
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
    mwa: {},
    drawer: undefined,
    items: [
      { titleKey: "main.home", icon: "mdi-image", link: "/" },
      { titleKey: "main.series", icon: "mdi-view-dashboard", link: "/series" },
      { titleKey: "main.about", icon: "mdi-help-box", link: "/about" }
    ],
    langs: [
      {
        resKey: "lang.fr",
        value: "fr"
      },
      {
        resKey: "lang.en",
        value: "en"
      }
    ]
  }),
  mounted() {
    this.mwa = this.$store.getters.mwaMeme;
  },
  methods: {
    logout: function() {
      this.$store.dispatch(AUTH_LOGOUT).then(() => {
        this.$router.push("/auth");
      });
    }
  },
  filters: {
    capitalize: function(value) {
      if (!value) return "";
      return value.toString().toUpperCase();
    }
  }
};
</script>

<style lang="scss" scoped>
.title {
  z-index: 1;
}

@keyframes move-twink-back {
  from {
    background-position: 0 0;
  }
  to {
    background-position: -10000px 5000px;
  }
}
@-webkit-keyframes move-twink-back {
  from {
    background-position: 0 0;
  }
  to {
    background-position: -10000px 5000px;
  }
}
@-moz-keyframes move-twink-back {
  from {
    background-position: 0 0;
  }
  to {
    background-position: -10000px 5000px;
  }
}
@-ms-keyframes move-twink-back {
  from {
    background-position: 0 0;
  }
  to {
    background-position: -10000px 5000px;
  }
}

.stars,
.twinkling {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.stars {
  background: rgb(23, 23, 23) url(../assets/stars.png) repeat;
  z-index: -1;
}

.twinkling {
  background: transparent url(../assets/twinkling.png) repeat top center;
  z-index: 0;

  -moz-animation: move-twink-back 200s linear infinite;
  -ms-animation: move-twink-back 200s linear infinite;
  -o-animation: move-twink-back 200s linear infinite;
  -webkit-animation: move-twink-back 200s linear infinite;
  animation: move-twink-back 200s linear infinite;
}
</style>
