import Vue from "vue";
import VueRouter from "vue-router";

import Home from "../views/Home.vue";
import Series from "../views/Series.vue";

import serie from "../components/serie";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home
  },
  {
    path: "/series",
    component: Series
  },
  {
    path: "/series/:id",
    component: serie
  }
];

const router = new VueRouter({
  routes
});

export default router;
