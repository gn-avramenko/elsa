/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.demo.remoting.restws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Random;
import java.util.UUID;

@Controller
public class RestWsController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private static final Logger log = LoggerFactory.getLogger(RestWsController.class);


//    @MessageMapping("/restws")
//    public void sendSpecific(@Payload String msg, Principal user, @Header("simpSessionId") String sessionId) throws Exception {
//        System.out.println(msg);
//        simpMessagingTemplate.convertAndSend("/topic", "hello world %s".formatted(UUID.randomUUID().toString()));
//        new Thread(){
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(2000L);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                simpMessagingTemplate.convertAndSend("/topic", "hello thread 1");
//                try {
//                    Thread.sleep(2000L);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                simpMessagingTemplate.convertAndSend("/topic", "hello thread 2");
//            }
//        }.start();
////        return "hello world";
////        simpMessagingTemplate.send("/app/topic", ms);
////        simpMessagingTemplate.convertAndSendToUser("123", "/ws/user/queue/specific-user", "hello world");
//    }

    @MessageMapping("/test")
    @SendToUser("/queue/specific-user")
    public String sendSpecific(
            @Payload String msg,
            Principal user) throws Exception {
        System.out.println(msg);
        return "hello specific";
//        simpMessagingTemplate.convertAndSendToUser(user.getName(), "/user/queue/specific-user", "hello specific");
    }
}
