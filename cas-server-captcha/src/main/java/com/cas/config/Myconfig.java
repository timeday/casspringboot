package com.cas.config;

import com.cas.controller.CaptchaController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 *   这个配置是空值,是为了让spring 加载 这个包下 标注了  @Service @Component @Controller 等注解的Bean
 *   并需要在resource/META-INF/spring.factories 中配置
 */
@Import({DataSourceAutoConfiguration.class})
@Configuration
@ComponentScan("com.cas")
public class Myconfig {

    @Bean
    @ConditionalOnMissingBean(name = "captchaController")
    public CaptchaController captchaController(){

        return new CaptchaController();
    }

}
