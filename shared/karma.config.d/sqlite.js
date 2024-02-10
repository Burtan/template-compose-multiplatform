;(function(config) {
    const path = require("path");
    const os = require("os");
    const worker = path.resolve("kotlin/sqlite-worker.js")

    config.files.push({
        pattern: worker,
        served: true,
        watched: false,
        included: false,
        nocache: false,
    });

    config.proxies["/sqlite-worker.js"] = path.join("/absolute/", worker)
})(config);
