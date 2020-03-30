import Vue from "vue";
import VueRouter from "vue-router";

import About from "../views/About";
import Auth from "../views/Auth";
import Home from "../views/Home";
import Main from "../views/Main";
import Series from "../views/Series";

import login from "../components/login";
import register from "../components/register";
import serie from "../components/serie";

Vue.use(VueRouter);

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
    ]
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
    ]
  }
];

const router = new VueRouter({
  routes
});

export default router;
