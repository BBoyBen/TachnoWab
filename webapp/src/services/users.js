import api from "../services/api";

import { User } from "../models/User";

export default {
  postAuth(login, pwd) {
    return api(false).post("utilisateur/auth", {
      login: login,
      motDePasse: pwd
    });
  },
  postUser(user) {
    api()
      .post("utilisateur", user)
      .then(response => {
        console.debug("response", response);
        // return true
      });
  },
  getUsers() {
    return {
      data: [
        { header: "Utilisateurs" },
        {
          id: "1",
          text: "mwa-meme",
          isReadOnly: true
        },
        {
          id: "2",
          text: "bénoit",
          isReadOnly: true
        }
      ]
    };
  },
  getMe() {
    return new Promise(resolve => {
      resolve(new User("0000", "Arnould", "Alexis", "admin"));
    });
  }
};
