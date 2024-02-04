;(function(config) {
    // adapted from https://github.com/karma-runner/karma/issues/3676#issuecomment-888244224
    function CrossOriginIsolationMiddlewareFactory(config) {
        return function crossOriginIsolation(req, res, next) {
            res.setHeader('Cross-Origin-Opener-Policy', 'same-origin');
            res.setHeader('Cross-Origin-Embedder-Policy', 'require-corp');
            next();
        };
    }

    config.plugins.push(
        { 'middleware:cross-origin-isolation': ['factory', CrossOriginIsolationMiddlewareFactory] }
    )
    config.beforeMiddleware = [(
        'cross-origin-isolation'
    )]
})(config);
