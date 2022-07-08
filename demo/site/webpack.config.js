const CopyWebpackPlugin = require('copy-webpack-plugin');

const path = require('path');

module.exports = {
  mode: 'development',
  entry: './src/index.ts',
  target: 'web',
  devtool: 'source-map',
  output: {
    path: path.resolve(__dirname, 'build'),
    filename: '[name]-module.js',
  },
  resolve: {
    extensions: ['.ts', '.js'], // resolve all the modules other than index.ts
  },
  module: {
    rules: [
      {
        loader: 'ts-loader',
        test: /\.ts?$/,
        options: { allowTsInNodeModules: true },
      },
    ],
  },
  plugins: [
    new CopyWebpackPlugin({
      patterns: [
        { from: './public' },
      ],
    }),
  ],
  devServer: {
    static: {
      directory: path.join(__dirname, 'build'),
    },
    proxy: {
      '/login': 'http://localhost:8086',
      '/logout': 'http://localhost:8086',
      '/auth': 'http://localhost:8086',
      '/remoting': 'http://localhost:8086',
    },
    compress: false,
    port: 3000,
  },
};
