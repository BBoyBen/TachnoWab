// import api from "../services/api";

export default {
  postAuth(login, pwd) {
    // return api().post("utilisateur/auth", { login: login, motDePasse: pwd });
    return new Promise(resolve =>
      setTimeout(() => {
        resolve(login != pwd);
      }, 2000)
    ); // timeOut is to fake waiting response for view effect ;)
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
        login: "mwa-meme",
        name: "Arnould",
        firstName: "Alexis"
      });
    });
  }
};
