;(function(config) {
    // increase timeout
    config.set({
        client: {
            mocha: {
                timeout: 240000
            }
        }
    });
})(config);