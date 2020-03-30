// import api from "../services/api";

export default {
  postAuth(login, pwd) {
    // return api().post("utilisateur/auth", { login: login, motDePasse: pwd });
    return new Promise(resolve => resolve(login != pwd));
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
      resolve({
        id: "1",
        username: "mwa-meme"
      });
    });
  }
};
