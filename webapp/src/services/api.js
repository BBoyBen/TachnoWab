import Vue from "vue";
import axios from "axios";

function errorResponseHandler(error) {
  if (error.response) {
    Vue.toasted.error(error, {
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
  }
}

export default () => {
  var api = axios.create({
    baseURL: "http://localhost:8080",
    withCredentials: false,
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json"
    }
  });
  api.interceptors.response.use(response => response, errorResponseHandler);
  return api;
};
