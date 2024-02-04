;(function(config) {
    const path = require("path");
    const os = require("os");
    const dist = path.resolve("../../node_modules/sql.js/dist/")
    const wasm = path.join(dist, "sql-wasm.wasm")
    const worker = path.resolve("kotlin/sqlite-worker.js")

    config.files.push({
        pattern: worker,
        served: true,
        watched: false,
        included: false,
        nocache: false,
    }, {
        pattern: wasm,
        served: true,
        watched: false,
        included: false,
        nocache: false,
    });

    config.proxies["/sqlite-worker.js"] = path.join("/absolute/", worker)
    config.proxies["/sql-wasm.wasm"] = path.join("/absolute/", wasm)


    // Adapted from: https://github.com/ryanclark/karma-webpack/issues/498#issuecomment-790040818
    const output = {
      path: path.join(os.tmpdir(), '_karma_webpack_') + Math.floor(Math.random() * 1000000),
    }
    config.set({
      webpack: {...config.webpack, output}
    });
    config.files.push({
      pattern: `${output.path}/**/*`,
      watched: false,
      included: false,
    });
})(config);