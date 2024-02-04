;(function(config) {
    // only on mac
    if (config.browsers.length == 1) {
        config.plugins.push("karma-safarinative-launcher")
        config.browsers.push("SafariNative")
        config.browsers = config.browsers.filter(e => e !== "Safari")
    }
})(config);
