// project/webpack.config.d/fs.js
config.resolve = {
    ...config.resolve,
    fallback: {
        fs: false,
        path: false,
        crypto: false,
    }
};
