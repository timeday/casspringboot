package com.cas.config;

import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.web.flow.CasWebflowConfigurer;
import org.apereo.cas.web.flow.config.CasWebflowContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;

////增加了@Import({DataSourceAutoConfiguration.class}),开启Spring Boot数据源自动配置，这样才可以使用使用jdbcTemplate
@Import({DataSourceAutoConfiguration.class})
@Configuration
@EnableConfigurationProperties(CasConfigurationProperties.class)
@AutoConfigureBefore(value = CasWebflowContextConfiguration.class)
@ComponentScan(basePackages = {"com.cas.*"})
public class SpringConfig {
    @Autowired
    private CasConfigurationProperties casProperties;

    @Autowired
    private FlowDefinitionRegistry loginFlowRegistry;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private FlowBuilderServices flowBuilderServices;

    @Bean
    public CasWebflowConfigurer customWebflowConfigurer() {
        final CaptchaWebflowConfigurer c = new CaptchaWebflowConfigurer(flowBuilderServices, loginFlowRegistry, applicationContext, casProperties);
        c.initialize();
        return c;
    }
}