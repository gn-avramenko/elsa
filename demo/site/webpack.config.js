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
        use: 'ts-loader',
        test: /\.ts?$/,
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
      '/restws': 'http://localhost:8086/restws',
    },
    compress: true,
    port: 3000,
  },
};
