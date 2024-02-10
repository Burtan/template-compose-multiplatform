// noinspection JSUnnecessarySemicolon
;(function(config) {
    config.devServer = {
        ...config.devServer,
        headers: {
            "Cross-Origin-Embedder-Policy": "require-corp",
            "Cross-Origin-Opener-Policy": "same-origin",
        }
    }
})(config);
