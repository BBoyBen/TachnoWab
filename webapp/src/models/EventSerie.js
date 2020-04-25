export class EventSerie {
  constructor(id, date, value, comment, tags) {
    this.id = id;
    this.date = date;
    this.value = value;
    this.comment = comment;
    this.tags = tags;
  }

  toPost(idSerie) {
    return new EventSerieToPost(this.value, this.comment, this.tags, idSerie);
  }
}

export class EventSerieToPost {
  constructor(value, comment, tags, idSerie) {
    this.valeur = value;
    this.commentaire = comment;
    this.tags = tags;
    this.idSerie = idSerie;
  }
}
