// noinspection JSUnnecessarySemicolon
// https://developer.chrome.com/docs/workbox/modules/workbox-webpack-plugin/
;(function(config) {
    const { GenerateSW } = require('workbox-webpack-plugin');

    config.plugins.push(
        new GenerateSW({
            maximumFileSizeToCacheInBytes: 10000000, // 10 mb
            cleanupOutdatedCaches: true,
            clientsClaim: true,
            skipWaiting: true,
        })
    );
})(config);
