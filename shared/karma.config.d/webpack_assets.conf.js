// This is needed to make webpack assets being recognized by karma
// See https://github.com/codymikol/karma-webpack/issues/498#issuecomment-1780963292
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
})(config);
