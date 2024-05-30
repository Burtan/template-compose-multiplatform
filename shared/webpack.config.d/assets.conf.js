// noinspection JSUnnecessarySemicolon
;(function(config) {
    config.module = {
        ...config.module,
        rules: [
            {
                test: /\.(png|jpg|gif|svg|eot|ttf|woff|json|txt)$/,
                type: 'asset/resource'
            },
        ],
    }
})(config);
