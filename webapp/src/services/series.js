import api from "../services/api";

export default {
  getSeries() {
    return api().get("/series/all");
  },

  postSerie(serie) {
    return api().post("serie", serie.toPost());
  },

  putSerie(serie) {
    return api().put(`serie/${serie.id}`, serie.toPost());
  },

  deleteSerie(serie) {
    return api().delete(`serie/${serie.id}`);
  }
};
