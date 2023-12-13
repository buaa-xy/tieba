module.exports = {
  transpileDependencies: ["vuetify"],
  devServer: {
    proxy: {
      "/api": {
        target: "http://localhost:8010",
        changeOrigin: true,
        ws: true,
        pathRewrite: {
          "^/api": "",
        },
      },
    },
  },
  publicPath: "/",
  chainWebpack: (config) => {
    config.plugin("html").tap((args) => {
      args[0].title = "Mark++";
      return args;
    });
  },
};
