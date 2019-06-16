package com.cas.demo.conf;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableWebMvc
@Configuration
@EnableSwagger2
@ComponentScan(basePackages = "com.cas.demo.controller")
public class SwaggerConfig {
    @Value("${swagger.show:true}")
    private boolean showAip;
    @Bean
    public Docket api() {
        ApiSelectorBuilder paths = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 自行修改为自己的包路径
                .paths(PathSelectors.any());
        if (showAip) {
                paths.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class));

        }else {
            paths.apis(RequestHandlerSelectors.none());
        }
        return paths.build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("zzz")
                .description("zzz接口文档")
                //服务条款网址
                .termsOfServiceUrl("https://github.com/")
                .version("1.0")
                .license("license").licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
                .contact(new Contact("satan", "http://www.baidu.com/", "jzzzg@azzz.com"))
                .build();
    }
}

