/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.demo.config;

import com.gridnine.elsa.demo.remoting.restws.CustomHandshakeHandler;
import com.gridnine.elsa.demo.remoting.restws.CustomHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//        config.enableSimpleBroker("/topic");
//        config.setApplicationDestinationPrefixes("/app");
////        config.setUserDestinationPrefix("/ws/user");
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/restws").setAllowedOriginPatterns("*");
//    }

//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//        config.enableSimpleBroker("/user/queue/specific-user");
//        config.setApplicationDestinationPrefixes("/app");
//        config.setUserDestinationPrefix("/user");
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/restws").setHandshakeHandler(new CustomHandshakeHandler()).setAllowedOriginPatterns("*");
//    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/user");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/restws").setAllowedOriginPatterns("*").addInterceptors(new CustomHandshakeInterceptor());
    }
}
