// noinspection JSUnnecessarySemicolon
;(function(config) {
    config.devServer = {
        ...config.devServer,
        "historyApiFallback": true
    };
})(config);
