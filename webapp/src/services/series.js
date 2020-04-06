// import api from "../services/api";

import { Serie } from "../models/Serie";

export default {
  getSeries() {
    // return api().get("/series/all");
    return {
      data: [
        new Serie(
          "1",
          "Titre",
          "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed non risus. Suspendisse lectus tortor, dignissim sit amet, adipiscing nec, ultricies sed, dolor. Cras elementum ultrices diam. Maecenas ligula massa, varius a, semper congue, euismod non, mi."
        )
      ]
    };
  },

  postSerie(serie) {
    return serie;
  },

  putSerie(serie) {
    return serie;
  },

  deleteSerie(serie) {
    console.debug(serie);
    return true;
  }
};
