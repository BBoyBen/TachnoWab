export class Serie {
  empty = false;

  constructor(id, title, description, sharedTo, isRO = undefined) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.sharedTo = sharedTo;
    this.isRO = isRO;
  }

  copy() {
    return new Serie(this.id, this.title, this.description, this.sharedTo);
  }

  toPost() {
    return new SerieToPost(this.title, this.description);
  }
}

export function empty() {
  var serie = new Serie();
  serie.empty = true;
  return serie;
}

export class SerieToPost {
  constructor(title, description) {
    this.titre = title;
    this.description = description;
  }
}
