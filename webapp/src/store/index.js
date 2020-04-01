import Vue from "vue";
import Vuex from "vuex";

import {
  AUTH_ERROR,
  AUTH_LOGOUT,
  AUTH_REQUEST,
  AUTH_SUCCESS,
  USER_ERROR,
  USER_REQUEST,
  USER_SUCCESS
} from "./actions";
import service from "../services/users";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    userCookie: localStorage.getItem("user-cookie") || "",
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
    [AUTH_SUCCESS]: (state, resp) => {
      state.status = "success";
      console.debug(resp); // TODO: delete line, current: prevent lint error
      state.userCookie = "oui"; // TODO: set cookie
    },
    [AUTH_ERROR]: state => {
      state.status = "error";
    },
    [AUTH_LOGOUT]: state => {
      state.profile = {};
      state.userCookie = null;
    },
    [USER_REQUEST]: state => {
      state.status = "loading";
    },
    [USER_SUCCESS]: (state, resp) => {
      state.status = "success";
      Vue.set(state, "profile", resp);
    },
    [USER_ERROR]: state => {
      state.status = "error";
    }
  },
  actions: {
    [AUTH_REQUEST]: ({ commit, dispatch }, user) => {
      return new Promise((resolve, reject) => {
        commit(AUTH_REQUEST);
        service
          .postAuth(user.username, user.password)
          .then(resp => {
            if (resp) {
              // localStorage.setItem("user-token", resp.token); // TODO: set Cookie
              commit(AUTH_SUCCESS, resp);
              dispatch(USER_REQUEST);
              resolve(resp);
            } else commit(AUTH_ERROR);
          })
          .catch(err => {
            commit(AUTH_ERROR, err);
            // localStorage.removeItem("user-token"); // TODO: remove Cookie
            reject(err);
          });
      });
    },
    [AUTH_LOGOUT]: ({ commit }) => {
      return new Promise(resolve => {
        commit(AUTH_LOGOUT);
        // localStorage.removeItem("user-token"); // TODO: remove Cookie
        resolve();
      });
    },
    [USER_REQUEST]: ({ commit, dispatch }) => {
      commit(USER_REQUEST);
      service
        .getMe()
        .then(resp => {
          commit(USER_SUCCESS, resp);
        })
        .catch(() => {
          commit(USER_ERROR);
          dispatch(AUTH_LOGOUT);
        });
    }
  }
});
