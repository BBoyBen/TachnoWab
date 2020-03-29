import api from "../services/api";

export default {
  postAuth(login, pwd) {
    return api().post("utilisateur/auth", { login: login, motDePasse: pwd });
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
  }
};
