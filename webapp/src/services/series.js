// import api from "../services/api";

export default {
  getSeries() {
    // return api().get("/series/all");
    return {
      data: [
        {
          id: "1",
          titre: "Titre",
          description:
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed non risus. Suspendisse lectus tortor, dignissim sit amet, adipiscing nec, ultricies sed, dolor. Cras elementum ultrices diam. Maecenas ligula massa, varius a, semper congue, euismod non, mi."
        }
      ]
    };
  }
};
