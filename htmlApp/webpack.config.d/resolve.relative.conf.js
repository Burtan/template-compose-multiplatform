;(function(config) {
    // needed for resolving moko-resources in tests
    config.resolve = {
        ...config.resolve,
        preferRelative: true
    }
})(config);
