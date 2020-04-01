module.exports = {
  transpileDependencies: ["vuetify"],

  devServer: {
    port: 9091
  },

  pluginOptions: {
    i18n: {
      locale: "fr",
      fallbackLocale: "en",
      localeDir: "locales",
      enableInSFC: true
    }
  }
};
