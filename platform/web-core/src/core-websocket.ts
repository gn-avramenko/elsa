export type WebSocketParams = {
    connectUrl: string
}

export type SubscriptionParams = {
    remoting: string;
    group: string;
    subscription: string;
    parameter?: any;
    handler: (event: any | undefined) => Promise<void> | void
}

// eslint-disable-next-line no-shadow
enum QueueItemType {
    SUBSCRIBE,
    UNSUBSCRIBE
}

type QueueItem = {
    type: QueueItemType
    callId: number,
    subscriptionParams?: SubscriptionParams,
    subscriptionId?: number
    resolve?: (result: any | undefined) => void
    reject?: (error: any | undefined) => void
}

export class WebSocketFacade {
    private params: WebSocketParams;

    private socket: WebSocket | null = null;

    private openingSocket = false;

    private processingQueue = false;

    private queue: QueueItem[] = [];

    private callId: number = 0;

    private currentCallId = -1;

    private currentResolve: ((result: any | undefined) => void) | null = null;

    private currentReject: ((result: any | undefined) => void) | null = null;

    private subscriptions: Map<number, (event: any | undefined) => Promise<void> | void> = new Map();

    constructor(params: WebSocketParams) {
      this.params = params;
    }

    private cleanupQueue(reason: string) {
      try {
        if (this.currentReject) {
          this.currentReject(reason);
        }
        this.currentReject = null;
        this.currentResolve = null;
        this.openingSocket = false;
        this.subscriptions.clear();
        this.queue.forEach((it) => it.reject && it.reject(reason));
      } catch (e) {
        // noop
      }
      this.queue = [];
    }

    private async processQueue() {
      if (this.processingQueue) {
        return;
      }
      if (!this.socket) {
        this.openingSocket = true;
        await new Promise<void>((resolve, reject) => {
          this.socket = new WebSocket(this.params.connectUrl);
          this.socket.onopen = () => {
            console.log('[ws open]');
            resolve();
          };
          this.socket.onclose = (event) => {
            console.log(`[ws close] code =${event.code} reason=${event.reason}`);
            this.cleanupQueue('closing socket');
            this.socket = null;
          };
          this.socket.onmessage = (event) => {
            const content = event.data as string;
            const data = JSON.parse(content);
            if (data.callId) {
              if (data.callId !== this.currentCallId) {
                this.cleanupQueue('wrong call id');
                return;
              }
              if (data.subscriptionId) {
                            this.currentResolve!!(data.subscriptionId);
                            return;
              }
                        this.currentResolve!!(undefined);
                        return;
            }
            const handler = this.subscriptions.get(data.subscriptionId);
            if (handler) {
              handler(data.event);
              return;
            }
            this.callId += 1;
            this.queue.push({
              type: QueueItemType.UNSUBSCRIBE,
              subscriptionId: data.subscriptionId,
              callId: this.callId,
            });
            this.processQueue();
          };
          this.socket.onerror = (error) => {
            console.error('[ws error]', error);
            if (this.openingSocket) {
              reject(error);
            }
            this.cleanupQueue('socket error');
          };
        });
      }
      const items = [...this.queue];
      for (const item of items) {
        if (!this.queue.length) {
          return;
        }
        this.currentCallId = item.callId;
        try {
          if (item.type === QueueItemType.UNSUBSCRIBE) {
            await new Promise<void>((resolve, reject) => {
              this.currentReject = reject;
              this.currentResolve = resolve;
              if (this.socket) {
                this.socket.send(JSON.stringify({
                  operation: 'unsubscribe',
                  callId: item.callId,
                  subscriptionId: item.subscriptionId,
                }));
              } else {
                reject(new Error('socket is closed'));
                this.currentReject = null;
                this.currentResolve = null;
              }
            });
            if (item.resolve) {
              item.resolve(null);
            }
            continue;
          }
          const subId = await new Promise<number>((resolve, reject) => {
            this.currentReject = reject;
            this.currentResolve = resolve;
            if (this.socket) {
              this.socket.send(JSON.stringify({
                operation: 'subscribe',
                callId: item.callId,
                remoting: item.subscriptionParams!!.remoting,
                group: item.subscriptionParams!!.group,
                subscription: item.subscriptionParams!!.subscription,
                parameter: item.subscriptionParams,
              }));
            } else {
              reject(new Error('socket is closed'));
              this.currentReject = null;
              this.currentResolve = null;
            }
          });
          this.subscriptions.set(subId, item.subscriptionParams!!.handler);
          if (item.resolve) {
            item.resolve(subId);
          }
        } catch (e) {
          if (item.reject) {
            item.reject(e);
          }
        } finally {
          const idx = this.queue.indexOf(item);
          this.queue.splice(idx, 1);
        }
      }
      if (this.queue.length) {
        this.processingQueue = false;
        await this.processQueue();
      }
    }

    async subscribe(params: SubscriptionParams): Promise<number> {
      // eslint-disable-next-line no-async-promise-executor
      return await new Promise<number>(async (resolve, reject) => {
        this.callId += 1;
        this.queue.push({
          type: QueueItemType.SUBSCRIBE,
          callId: this.callId,
          reject,
          resolve,
          subscriptionParams: params,
        });
        await this.processQueue();
      });
    }

    async unsubscribe(subscriptionId: number): Promise<void> {
      // eslint-disable-next-line no-async-promise-executor
      return await new Promise<void>(async (resolve, reject) => {
        this.callId += 1;
        this.queue.push({
          type: QueueItemType.UNSUBSCRIBE,
          callId: this.callId,
          reject,
          resolve,
          subscriptionId,
        });
        await this.processQueue();
      });
    }
}

export class SubsequentSubscriptionsManager {
    private delegate: WebSocketFacade;

    private subscriptions: Map<string, number> = new Map<string, number>();

    constructor(facade: WebSocketFacade) {
      this.delegate = facade;
    }

    private toKey(remoting: string, group: string, subscription: string): string {
      return `${remoting}:${group}:${subscription}`;
    }

    async subscribe(params: SubscriptionParams): Promise<void> {
      const key = this.toKey(params.remoting, params.group, params.subscription);
      let subscriptionId = this.subscriptions.get(key);
      if (subscriptionId) {
        this.subscriptions.delete(key);
        await this.delegate.unsubscribe(subscriptionId);
      }
      subscriptionId = await this.delegate.subscribe(params);
      if (this.subscriptions.has(key)) {
        await this.delegate.unsubscribe(subscriptionId);
        return;
      }
      this.subscriptions.set(key, subscriptionId);
    }

    async unsubscribe(remoting: string, group: string, subscription: string) : Promise<void> {
      const key = this.toKey(remoting, group, subscription);
      const subscriptionId = this.subscriptions.get(key);
      if (subscriptionId) {
        await this.delegate.unsubscribe(subscriptionId);
      }
      const sub2 = this.subscriptions.get(key);
      if (sub2 && subscriptionId && sub2 !== subscriptionId) {
        await this.unsubscribe(remoting, group, subscription);
        return;
      }
      if (subscriptionId) {
        this.subscriptions.delete(key);
      }
    }
}
