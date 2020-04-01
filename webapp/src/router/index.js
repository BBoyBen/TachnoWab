import Vue from "vue";
import VueRouter from "vue-router";
import store from "../store";

import About from "../views/About";
import Auth from "../views/Auth";
import Home from "../views/Home";
import Main from "../views/Main";
import Series from "../views/Series";

import login from "../components/login";
import register from "../components/register";
import serie from "../components/serie";

Vue.use(VueRouter);

const ifNotAuthenticated = (to, from, next) => {
  if (!store.getters.isAuthenticated) {
    next();
    return;
  }
  next("/");
};

const ifAuthenticated = (to, from, next) => {
  if (store.getters.isAuthenticated) {
    next();
    return;
  }
  next("/auth");
};

const routes = [
  {
    path: "/",
    component: Main,
    children: [
      {
        path: "",
        component: Home
      },
      {
        path: "about",
        component: About
      },
      {
        path: "series",
        component: Series
      },
      {
        path: "series/:id",
        component: serie
      }
    ],
    beforeEnter: ifAuthenticated
  },
  {
    path: "/auth",
    component: Auth,
    children: [
      {
        path: "",
        component: login
      },
      {
        path: "register",
        component: register
      }
    ],
    beforeEnter: ifNotAuthenticated
  }
];

const router = new VueRouter({
  routes
});

export default router;
