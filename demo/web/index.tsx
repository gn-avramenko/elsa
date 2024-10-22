import { Configuration } from '../../platform/web-core';
import { PreloaderMiddleware } from '../../platform/web-core/src/core-remoting';
import DemoMainApi from './src/generated/api/DemoMainApi';

const preloader = new PreloaderMiddleware(
  {
    showPreloader: () => console.log('preloader is shown'),
    hidePreloader: () => console.log('preloader is hidden'),
  },
  {
    delay: 1000,
  },
);
const demoApi = new DemoMainApi(new Configuration({
  basePath: '/api',
  middleware: [preloader],
}));

async function test() {
  const result = await demoApi.testRequest({
    testParam: 'hello world',
  });
  console.log(result);
}

test();
