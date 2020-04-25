export class User {
  constructor(id, name, firstName, login) {
    this.id = id;
    this.name = name;
    this.firstName = firstName;
    this.login = login;
  }
}

export class UserToPost {
  constructor(name, firstName, login, pwd) {
    this.nom = name;
    this.prenom = firstName;
    this.login = login;
    this.motDePasse = pwd;
  }
}
