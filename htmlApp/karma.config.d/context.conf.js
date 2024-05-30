;(function(config) {
    // set custom context file
    config.set({
        customContextFile: "./kotlin/test.html"
    });
    config.files.push("./kotlin/index.css")
    config.files.push("./kotlin/theme/*.css")
    // add css referenced by test.html to served files because webpack does not import it
    config.proxies["/index.css"] = "./kotlin/index.css"
    config.proxies["/theme/theme.css"] = "./kotlin/theme/theme.css"
    config.proxies["/tokens.css"] = "./kotlin/theme/tokens.css"
    config.proxies["/colors.module.css"] = "./kotlin/theme/colors.module.css"
    config.proxies["/typography.module.css"] = "./kotlin/theme/typography.module.css"
    config.proxies["/theme.light.css"] = "./kotlin/theme/theme.light.css"
    config.proxies["/theme.dark.css"] = "./kotlin/theme/theme.dark.css"
})(config);
