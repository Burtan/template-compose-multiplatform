;(function(config) {
    // increase timeout
    config.set({
        client: {
            mocha: {
                timeout: 60000
            }
        }
    });
})(config);