package group12.storytime.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class WebSocketConfigTest {

    private WebSocketConfig webSocketConfig;
    private MessageBrokerRegistry messageBrokerRegistry;
    private StompEndpointRegistry stompEndpointRegistry;
    private StompWebSocketEndpointRegistration endpointRegistration;

    @BeforeEach
    public void setUp() {
        // 初始化配置类和模拟对象
        webSocketConfig = new WebSocketConfig();
        messageBrokerRegistry = mock(MessageBrokerRegistry.class);
        stompEndpointRegistry = mock(StompEndpointRegistry.class);
        endpointRegistration = mock(StompWebSocketEndpointRegistration.class);

        // 模拟 addEndpoint() 方法的链式调用
        when(stompEndpointRegistry.addEndpoint(anyString())).thenReturn(endpointRegistration);
        when(endpointRegistration.setAllowedOriginPatterns(anyString())).thenReturn(endpointRegistration);
    }

    @Test
    public void testConfigureMessageBroker() {
        // 调用 configureMessageBroker() 方法
        webSocketConfig.configureMessageBroker(messageBrokerRegistry);

        // 验证是否正确配置了消息代理
        verify(messageBrokerRegistry, times(1)).enableSimpleBroker("/topic");
        verify(messageBrokerRegistry, times(1)).setApplicationDestinationPrefixes("/app");
    }

    @Test
    public void testRegisterStompEndpoints() {
        // 调用 registerStompEndpoints() 方法
        webSocketConfig.registerStompEndpoints(stompEndpointRegistry);

        // 验证是否正确调用了 addEndpoint() 和 setAllowedOriginPatterns()
        verify(stompEndpointRegistry, times(1)).addEndpoint("/ws");
        verify(endpointRegistration, times(1)).setAllowedOriginPatterns("*");
        verify(endpointRegistration, times(1)).withSockJS();
    }
}
