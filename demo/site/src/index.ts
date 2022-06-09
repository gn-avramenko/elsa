import {
  checkAuth, login, logout, setLogged,
} from './feature/auth';
import { connect, deactivate, sendMessage } from './feature/ws';

const w = window as any;
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
  await connect();
  sendMessage();
  // setTimeout(() => deactivate(), 1000);
};
w.deactivate = () => deactivate();

w.connect = () => connect();

w.logout = async () => {
  await logout();
  setLogged(await checkAuth());
};
