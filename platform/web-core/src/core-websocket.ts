import {Configuration, getSubscriptionDescription} from "./core-metadata-provider";

export type WebSocketConfiguration = {
    endPoint: string
}

export default class WebSocketAPI {

    private webSocket: WebSocket | null = null

    private webSocketInitializing = false

    private webSocketInitializationPromise: Promise<any | null> | null = null

    constructor(protected configuration: WebSocketConfiguration, protected serviceConfiguration:Configuration) {
    }

    public async subscribe(remotingId: string, groupId:string, subscriptionId: string, parameter?: any): Promise<string> {
        if (!this.webSocket) {
            if (!this.webSocketInitializing) {
                this.webSocketInitializing = true
                const self = this
                this.webSocketInitializationPromise = new Promise((resolve, reject) => {
                  let url = `${this.configuration.endPoint}?remotingId=${remotingId}&groupId=${groupId}&subscriptionId=${subscriptionId}`
                  this.webSocket = new WebSocket(url)
                    this.webSocket.onopen = function () {
                        self.webSocketInitializing = false;
                        resolve(null)
                        console.log('web socket connected')
                    };
                    this.webSocket.onmessage = function (event) {
                        console.log('Received: ' + event.data);
                    };
                    this.webSocket.onclose = function (event) {
                        self.webSocketInitializing = false
                        self.webSocket = null
                        reject(event.code)
                        console.log('Info: WebSocket connection closed, Code: ' + event.code + (event.reason == "" ? "" : ", Reason: " + event.reason));
                    };
                })
                await this.webSocketInitializationPromise
            }
            const sp = this.createSubscriptionPromise(remotingId, groupId, subscriptionId, parameter)
            this.webSocketInitializationPromise = this.webSocketInitializationPromise?.then(() => sp, ()=>sp)!!
            return sp
        }
        return this.createSubscriptionPromise(remotingId, groupId, subscriptionId, parameter)
    }

    private createSubscriptionPromise(remotingId:string, groupId:string, subscriptionId: string, parameter?: any): Promise<string> {
        return new Promise(async (resolve, reject) => {
            if(!this.webSocket){
                reject('web socket is not initialized')
                return
            }
            const descr = await getSubscriptionDescription(this.serviceConfiguration, remotingId, groupId, subscriptionId)
            const payload = {
                remotingId,
                groupId,
                su
            }
            descr.parameterClassName
            this.webSocket.send()
        })
    }

}



