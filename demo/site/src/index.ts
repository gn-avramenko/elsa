import { serverCall } from './feature/remoting';

const w = window as any;
w.processPublic = async () => {
  const result = await serverCall('demo', 'test', 'server-call', {
    param: 'test',
  }, false, null);
  console.log(result);
};
