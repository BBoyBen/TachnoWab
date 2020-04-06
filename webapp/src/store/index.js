import Vue from "vue";
import Vuex from "vuex";

import { AUTH_ERROR, AUTH_LOGOUT, AUTH_REQUEST, AUTH_SUCCESS } from "./actions";
import service from "../services/users";
import { User } from "../models/User";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    userCookie: localStorage.getItem("user-cookie") || "",
    status: "",
    profile: User
  },
  getters: {
    mwaMeme: state => state.profile,
    isAuthenticated: state => !!state.userCookie,
    authStatus: state => state.status
  },
  mutations: {
    [AUTH_REQUEST]: state => {
      state.status = "loading";
    },
    [AUTH_SUCCESS]: (state, user) => {
      state.status = "success";
      Vue.set(state, "profile", user);
      state.userCookie = "oui"; // TODO: set cookie
    },
    [AUTH_ERROR]: state => {
      state.status = "error";
    },
    [AUTH_LOGOUT]: state => {
      state.profile = null;
      state.userCookie = null;
    }
  },
  actions: {
    [AUTH_REQUEST]: ({ commit }, user) => {
      return new Promise((resolve, reject) => {
        commit(AUTH_REQUEST);
        service
          .postAuth(user.username, user.password)
          .then(response => {
            if (response) {
              // localStorage.setItem("user-token", resp.token); // TODO: set Cookie
              commit(
                AUTH_SUCCESS,
                new User(
                  response.data.id,
                  response.data.nom,
                  response.data.prenom,
                  response.data.login
                )
              );
              resolve(response);
            } else commit(AUTH_ERROR);
          })
          .catch(error => {
            commit(AUTH_ERROR, error);
            // localStorage.removeItem("user-token"); // TODO: remove Cookie
            reject(error);
          });
      });
    },
    [AUTH_LOGOUT]: ({ commit }) => {
      return new Promise(resolve => {
        commit(AUTH_LOGOUT);
        // localStorage.removeItem("user-token"); // TODO: remove Cookie
        resolve();
      });
    }
  }
});
