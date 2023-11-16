package bitedu.bipa.simplesignalarm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/alarm/ws")
                .setAllowedOrigins("http://localhost:3000", "https://simple-sign-frontend-orpin.vercel.app","ws://localhost:3000","ws://simple-sign-frontend-orpin.vercel.app", "wss://simple-sign-frontend-orpin.vercel.app")
                .withSockJS();
    }
}
