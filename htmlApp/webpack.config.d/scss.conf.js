// noinspection JSUnnecessarySemicolon
;(function(config) {
    config.module.rules.push(
        {
            test: /\.css$/i,
            use: ["style-loader", "css-loader", "sass-loader"],
        },
    )
})(config);
