;(function(config) {
    // add webpack files to karma files
    function KarmaWebpackOutputFramework(config) {
        // This controller is instantiated and set during the preprocessor phase.
        const controller = config.__karmaWebpackController;

        // only if webpack has instantiated its controller
        if (!controller) {
            console.warn(
                "Webpack has not instantiated controller yet.\n" +
                "Check if you have enabled webpack preprocessor and framework before this framework"
            )
            return
        }

        config.files.push({
            pattern: `${controller.outputPath}/**/*`,
            included: false,
            served: true,
            watched: false
        })
    }

    const KarmaWebpackOutputPlugin = {
        'framework:webpack-output': ['factory', KarmaWebpackOutputFramework],
    };

    config.plugins.push(KarmaWebpackOutputPlugin);
    config.frameworks.push("webpack-output");

    // needed since kotlin 2.1.0
    config.proxies = {
        "/": "/base/kotlin/"
    }
    config.files.push(
        {
            "pattern": "./kotlin/**/*",
            "watched": false,
            "included": false
        }
    )
})(config);
