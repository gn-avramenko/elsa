import { BaseAPI, Configuration } from 'elsa-core';
import { println } from '@/imp.ts';
// import DemoMainApi from './generated/api/DemoMainApi.ts';
// import {Configuration, PreloaderMiddleware} from "elsa-core";
//
//
// const preloader = new PreloaderMiddleware(
//   {
//     showPreloader: () => console.log('preloader is shown'),
//     hidePreloader: () => console.log('preloader is hidden'),
//   },
//   {
//     delay: 1000,
//   },
// );
//
// const demoApi = new DemoMainApi(new Configuration({
//   basePath: '/api',
//   middleware: [preloader],
// }));
// async function test() {
//   const result = await demoApi.testRequest({
//     testParam: 'hello world',
//   });
//   console.log(result);
// }
//
// test();

class TestApi extends BaseAPI {

}
const test = new TestApi(new Configuration({}), {
  groupId: 'test',
  remotingId: 'test',
});
println(test);
