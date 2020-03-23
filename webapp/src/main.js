import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";
import toasted from "vue-toasted";
import "../node_modules/material-icons/iconfont/material-icons.scss";

Vue.config.productionTip = false;

Vue.use(toasted, { iconPack: ["material"] });

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount("#app");
