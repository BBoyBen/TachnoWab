import api from "../services/api";

export default {
  getSeries() {
    return api().get("/series/user");
  },

  postSerie(serie) {
    return api().post("serie", serie.toPost());
  },

  putSerie(newOne) {
    return api().put(`serie/${newOne.id}`, newOne.toPost());
  },

  deleteSerie(serie) {
    return api().delete(`serie/${serie.id}`);
  }
};
