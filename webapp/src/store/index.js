import Vue from "vue";
import Vuex from "vuex";
import Cookie from "js-cookie";
import createPersistedState from "vuex-persistedstate";

import { AUTH_ERROR, AUTH_LOGOUT, AUTH_REQUEST, AUTH_SUCCESS } from "./actions";
import service from "../services/users";
import { User } from "../models/User";

Vue.use(Vuex);

export default new Vuex.Store({
  plugins: [createPersistedState()],
  state: {
    userCookie: Cookie.get("persist") || "",
    status: "",
    profile: {}
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
      state.userCookie = user.id;
    },
    [AUTH_ERROR]: state => {
      state.status = "error";
      Cookie.remove("persist");
    },
    [AUTH_LOGOUT]: state => {
      state.profile = null;
      state.userCookie = "";
      Cookie.remove("persist");
    }
  },
  actions: {
    [AUTH_REQUEST]: ({ commit }, { login, password }) => {
      return new Promise((resolve, reject) => {
        commit(AUTH_REQUEST);
        service
          .postAuth(login, password)
          .then(response => {
            if (response) {
              Cookie.set("persist", response.data.id, { expires: 7 });
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
            reject(error);
          });
      });
    },
    [AUTH_LOGOUT]: ({ commit }) => {
      return new Promise(resolve => {
        commit(AUTH_LOGOUT);
        resolve();
      });
    }
  }
});
