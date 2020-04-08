import Vue from "vue";
import axios from "axios";
import i18n from "../plugins/i18n.js";

function errorResponseHandler(error) {
  if (error.response) {
    Vue.toasted.error(i18n.t(`error.${error.response.status}`), {
      icon: "error_outline",
      position: "bottom-right",
      duration: 4200,
      action: {
        icon: "close",
        onClick: (e, toastObject) => {
          toastObject.goAway(0);
        }
      }
    });
    return Promise.reject(error);
  }
}

export default (autoErrorHandling = true) => {
  var api = axios.create({
    baseURL: "http://localhost:8080",
    withCredentials: false,
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json"
    }
  });
  if (autoErrorHandling)
    api.interceptors.response.use(response => response, errorResponseHandler);
  return api;
};
