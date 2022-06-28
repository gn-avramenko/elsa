import {
  checkAuth, login, logout, setLogged,
} from './feature/auth';
import { connect, deactivate } from './feature/ws';
import { callRawRest } from './feature/sse';

const w = window as any;
w.processPublic = async () => {
  const result = await callRawRest('public2', 'request', {});
  console.log(result);
};

w.processPrivate = async () => {
  const authorized = await checkAuth();
  if (!authorized) {
    await login();
  }
  const result = await callRawRest('private', 'request', {});
  console.log(result);
  logout();
};

w.process = async () => {
  const authorized = await checkAuth();
  if (!authorized) {
    await login();
  }
  if (!(await checkAuth())) {
    throw new Error('unable to login');
  }
  setLogged(authorized);
  console.log(authorized);
  // await connect();
  // sendMessage();
  // setTimeout(() => deactivate(), 1000);
};
w.deactivate = () => deactivate();

w.connect = () => connect();

w.logout = async () => {
  await logout();
  setLogged(await checkAuth());
};
