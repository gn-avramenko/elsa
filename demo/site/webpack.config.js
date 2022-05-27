const HtmlWebpackPlugin = require('html-webpack-plugin');

var path = require('path');

module.exports = {
  mode: 'development',
  entry: './src/index.ts',
  target: "web",
  devtool: 'source-map',
  output: {
    path: path.resolve(__dirname, 'build'),
    filename: '[name].js',
    chunkFilename: '[id].[chunkhash].js'
  },
  resolve: {
    extensions: ['.ts', '.js'] //resolve all the modules other than index.ts
  },
  module: {
    rules: [
      {
        use: 'ts-loader',
        test: /\.ts?$/
      }
    ]
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: path.resolve(__dirname, './public/index.html'),
      filename: 'index.html',
    }),
  ],
  devServer: {
    static: {
      directory: path.join(__dirname, 'build'),
    },
    compress: true,
    port: 3000,
  },
}
