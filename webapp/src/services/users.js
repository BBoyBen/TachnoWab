import api from "../services/api";

import { User } from "../models/User";

export default {
  postAuth(login, pwd) {
    return api(false).post("utilisateur/auth", {
      login: login,
      motDePasse: pwd
    });
  },
  async postUser(user) {
    const response = await api().post("utilisateur", user);
    return response.status == 201;
  },
  getUsers() {
    return api().get("utilisateurs");
  },
  getMe() {
    return new Promise(resolve => {
      resolve(new User("0000", "Arnould", "Alexis", "admin"));
    });
  }
};
