import api from "../services/api";
import { ShareToPost } from "../models/Share";

export default {
  getShares() {
    return api().get("partages");
  },

  getSharesForSerie(id) {
    return api().get(`partages/serie/${id}`);
  },

  postSharesForSerie(serie) {
    if (serie.sharedTo) {
      serie.sharedTo.forEach(s => {
        api().post(
          "partage",
          new ShareToPost(s.isReadOnly, s.id, s.text, serie.id)
        );
      });
    }
  }
};
