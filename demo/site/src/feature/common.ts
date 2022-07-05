// eslint-disable-next-line import/prefer-default-export,max-classes-per-file
export interface HasClassName {
  className: string;
}

export class RemotingError {
  status: number;

  message?: string;

  details?: string;

  constructor(status: number, message?: string, details?: string) {
    this.status = status;
    this.message = message;
    this.details = details;
  }
}

export type ErrorHandler = {
  // eslint-disable-next-line no-unused-vars
  onError: (er: any) => void
}

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

// eslint-disable-next-line no-unused-vars
export class RegistryItemType<T> {
  id: string;

  constructor(id: string) {
    this.id = id;
  }
}

export interface RegistryItem<T> {
  getType(): RegistryItemType<T>;
  getId(): string;
}

class Registry {
  private reg = new Map<string, Map<string, RegistryItem<any>>>();

  register(item: RegistryItem<any>) {
    let items = this.reg.get(item.getType().id);
    if (items == null) {
      items = new Map<string, RegistryItem<any>>();
      this.reg.set(item.getType().id, items);
    }
    items.set(item.getId(), item);
  }

  get<T>(itemType: RegistryItemType<T>, id:string) {
    return this.reg.get(itemType.id)?.get(id) as unknown as T|null;
  }

  allOf<T>(itemType: RegistryItemType<T>) {
    const result = this.reg.get(itemType.id)?.values();
    return result ? Array.of(result) : [];
  }
}

export const registry = new Registry();
