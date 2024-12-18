;(function(config) {
    // settings for chrome headless
    config.browsers = config.browsers
        .map(browser => {
            if (browser === 'ChromeHeadless') {
                return 'ChromeCustom'
            } else {
                return browser
            }
        })

    config.customLaunchers = {
        ...config.customLaunchers,
        ChromeCustom: {
            base: 'ChromeHeadless',
            flags: [
                '--accept-lang=de',
                //'--font-render-hinting=none', // no changes
                //'--force-color-profile=generic-rgb', // no changes
                //'--enable-font-antialiasing=false', // no changes
                '--disable-font-subpixel-positioning',
            ]
        }
    }
})(config);
