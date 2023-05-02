// eslint-disable-next-line import/prefer-default-export
export const generateUUID = () => {
  const s: string[] = [];
  const hexDigits = '0123456789abcdef';
  for (let i = 0; i <= 36; i += 1) {
    const r = Math.round(Math.random() * 0x10);
    const substr = hexDigits.substring(r, r + 1);
    s.push(substr);
  }
  s[14] = '4';
  s[8] = '-';
  s[13] = '-';
  s[18] = '-';
  s[23] = '-';
  return s.join('');
};

export const formatDigit = (number:number, scale:number) => {
  let remaining = Math.round(number);
  let result = '';
  let digit = scale;
  while (digit >= 0) {
    const part = Math.abs(Math.floor(remaining / 10 ** digit));
    result += part;
    digit -= 1;
    remaining -= part * 10 ** digit;
  }
  if (number < 0) {
    return `-${result}`;
  }
  return result;
};

// eslint-disable-next-line max-len
export const str2Bytes = (base64String: string) => Uint8Array.from(atob(base64String), (c) => c.charCodeAt(0));

export const bytes2Str = (u8: Uint8Array) => {
  const decoder = new TextDecoder('utf8');
  return btoa(decoder.decode(u8));
};
