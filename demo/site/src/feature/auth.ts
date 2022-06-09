let logged = false;
export const isLogged = () => logged;

export const setLogged = (value:boolean) => {
  logged = value;
};

export const login = async () => fetch('/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded',
  },
  body: 'username=user&password=password',
});

export const logout = () => fetch('/logout', {
  method: 'GET',
  redirect: 'manual',
});

export const checkAuth = async () => {
  const res = await fetch('/auth/check', {
    method: 'GET',
    redirect: 'manual',
  });
  return await res.text() === 'true';
};
