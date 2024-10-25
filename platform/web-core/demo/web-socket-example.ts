import {SubsequentSubscriptionsManager, WebSocketFacade} from "../src/core-websocket";

const webSocketFacade = new WebSocketFacade({
    connectUrl: "ws://localhost:8080/websocket"
})

const getElementById = (id:string)  =>{
    return document.getElementById(`#${id}`) as any
}
const subsequentSubscriptionsManager = new SubsequentSubscriptionsManager(webSocketFacade)

async function subscribe(){
    await subsequentSubscriptionsManager.subscribe({
        remoting: "ldocs-core",
        group: "common",
        subscription: "document-change",
        parameter: {
            directoryId: getElementById("parameter").value
        },
        handler: (event) =>{
            const elm = getElementById("greetings")
            elm.innerHTML = elm.innerHTML+`<tr><td>changed ${event.documentId}</td></tr>`
        }
    })
    getElementById("web-socket-status").value = "Connected"
    getElementById("subscription-status").value = "Subscribed"
}

async function unsubscribe(){
    await subsequentSubscriptionsManager.unsubscribe("ldocs-core", "common", "document-change")
    getElementById("subscription-status").value = "Unsubscribed"
}

async function init(){
    getElementById("web-socket-status").value = "Not connected"
    getElementById("subscribe").onclick = subscribe
    getElementById("unsubscribe").onclick = unsubscribe
}
document.addEventListener("DOMContentLoaded", function() {
    init()
});

