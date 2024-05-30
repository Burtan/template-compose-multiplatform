// noinspection JSUnnecessarySemicolon
;(function(config) {
    config.module = {
        ...config.module,
        rules: [
            {
                test: /\.(png|jpg|gif|svg|eot|ttf|woff|json|txt|woff2)$/,
                type: 'asset/resource'
            },
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader']
            },
            {
                test: /\.s[ac]ss$/i,
                use: [
                    // Creates `style` nodes from JS strings
                    "style-loader",
                    // Translates CSS into CommonJS
                    "css-loader",
                    // Compiles Sass to CSS
                    "sass-loader",
                ],
            },
        ],
    }
})(config);
