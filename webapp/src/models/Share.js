export class Share {
  constructor(idUser, userName, isReadOnly) {
    this.id = idUser;
    this.text = userName;
    this.isReadOnly = isReadOnly;
  }
}

export class ShareToPost {
  constructor(isReadOnly, idUser, login, idSerie) {
    this.lectureSeule = isReadOnly;
    this.idUtilisateur = idUser;
    this.loginUtilisateur = login;
    this.idSerie = idSerie;
  }
}
