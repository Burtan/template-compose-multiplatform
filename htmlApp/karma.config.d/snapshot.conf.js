;(function(config) {
    config.frameworks.push("snapshot")
    config.frameworks.push("mocha-snapshot")
    config.preprocessors["../../../../webApp/src/jsTest/snapshots/**/*.md"] = ["snapshot"]
    config.files.push("../../../../webApp/src/jsTest/snapshots/**/*.md")

    function resolve(basePath, suiteName) {
      const path = require("path");
      const base = basePath + "/../../../../webApp/src/jsTest/";
      return path.join(base, "snapshots", suiteName + ".md");
    }

    config.set({
      snapshot: {
        update: false,           // Run snapshot tests in UPDATE mode (default: false)
        prune: false,           // Prune unused snapshots (default: false)
        format: "indented-md",  // Snapshot format (default: md)
        checkSourceFile: false,  // Checks existince of the source file associated with tests (default: false)
        pathResolver: resolve,  // Custom path resolver,
        limitUnusedSnapshotsInWarning: -1  // Limit number of unused snapshots reported in the warning
                                           // -1 means no limit

      }
    });
})(config);