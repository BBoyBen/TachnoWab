import api from "../services/api";

export default {
  getSeries() {
    return api().get("/series/user");
  },

  getSerieById(id) {
    return api(false).get(`/serie/${id}`);
  },
  getEventsBySerieId(id) {
    return api().get(`/evenements/serie/${id}`);
  },

  postSerie(serie) {
    return api().post("serie", serie.toPost());
  },
  postEvent(serie, event) {
    return api().post("evenement", event.toPost(serie.id));
  },

  putSerie(newOne) {
    return api().put(`serie/${newOne.id}`, newOne.toPost());
  },

  deleteSerie(serie) {
    return api().delete(`serie/${serie.id}`);
  }
};
