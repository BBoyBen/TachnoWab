import Vue from "vue";
import axios from "axios";
import i18n from "../plugins/i18n.js";

function errorResponseHandler(error) {
  console.error(error.response);
  var code;
  if (error.response) {
    code = `error.${error.response.status}`;
  } else {
    code = "error.network";
  }
  Vue.toasted.error(i18n.t(code), {
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

export default (autoErrorHandling = true) => {
  var api = axios.create({
    baseURL: "http://localhost:8080",
    withCredentials: true,
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json"
    }
  });
  if (autoErrorHandling)
    api.interceptors.response.use(response => response, errorResponseHandler);
  return api;
};
