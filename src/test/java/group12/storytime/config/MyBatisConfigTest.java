package group12.storytime.config;

import group12.storytime.typehandler.UUIDTypeHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

public class MyBatisConfigTest {

    private MyBatisConfig myBatisConfig;

    @Mock
    private Configuration configuration;

    @Mock
    private TypeHandlerRegistry typeHandlerRegistry;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        myBatisConfig = new MyBatisConfig();

        // 模拟 Configuration 的 TypeHandlerRegistry
        when(configuration.getTypeHandlerRegistry()).thenReturn(typeHandlerRegistry);
    }

    @Test
    public void testMybatisConfigurationCustomizer() {
        // 获取自定义配置器 Bean
        ConfigurationCustomizer customizer = myBatisConfig.mybatisConfigurationCustomizer();

        // 执行自定义配置
        customizer.customize(configuration);

        // 验证 UUIDTypeHandler 被正确注册
        verify(typeHandlerRegistry, times(1)).register(UUID.class, UUIDTypeHandler.class);
    }
}
