module.exports = {
  transpileDependencies: ["vuetify"],
  devServer: {
    port: 9091,
    proxy: {
      "/*": {
        target: "http://localhost:8080",
        secure: false
      }
    }
  }
};
