package com.cas.config;

import org.apereo.cas.authentication.RememberMeUsernamePasswordCredential;
import org.apereo.cas.authentication.UsernamePasswordCredential;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.web.flow.CasWebflowConstants;
import org.apereo.cas.web.flow.configurer.DefaultWebflowConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.engine.ViewState;
import org.springframework.webflow.engine.builder.BinderConfiguration;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;

import java.util.Properties;

public class CaptchaWebflowConfigurer extends DefaultWebflowConfigurer {



    public CaptchaWebflowConfigurer(FlowBuilderServices flowBuilderServices, FlowDefinitionRegistry loginFlowDefinitionRegistry, ApplicationContext applicationContext, CasConfigurationProperties casProperties) {
        super(flowBuilderServices, loginFlowDefinitionRegistry, applicationContext, casProperties);
    }


    @Override
    protected void createRememberMeAuthnWebflowConfig(Flow flow) {

        String captchaEnabled=null;
        try {
            Properties properties = PropertiesLoaderUtils.loadAllProperties("my.properties");
            captchaEnabled=properties.getProperty("myDefition.captchaEnabled");
        }catch (Exception e){
            e.printStackTrace();
        }
        //不带验证码
        if(captchaEnabled==null && casProperties.getTicket().getTgt().getRememberMe().isEnabled()){
            createFlowVariable(flow, CasWebflowConstants.VAR_ID_CREDENTIAL, RememberMeUsernamePasswordCredential.class);
            final ViewState state = getState(flow, CasWebflowConstants.STATE_ID_VIEW_LOGIN_FORM, ViewState.class);
            final BinderConfiguration cfg = getViewStateBinderConfiguration(state);
            cfg.addBinding(new BinderConfiguration.Binding("rememberMe", null, false));
            cfg.addBinding(new BinderConfiguration.Binding("captcha", null, true));
            //带验证码
        }else if(captchaEnabled !=null && "true".equals(captchaEnabled) && casProperties.getTicket().getTgt().getRememberMe().isEnabled()){
            createFlowVariable(flow, CasWebflowConstants.VAR_ID_CREDENTIAL, RememberMeUsernamePasswordCaptchaCredential.class);
            final ViewState state = getState(flow, CasWebflowConstants.STATE_ID_VIEW_LOGIN_FORM, ViewState.class);
            final BinderConfiguration cfg = getViewStateBinderConfiguration(state);
            cfg.addBinding(new BinderConfiguration.Binding("rememberMe", null, false));
            cfg.addBinding(new BinderConfiguration.Binding("captcha", null, true));
        }else{
            createFlowVariable(flow, CasWebflowConstants.VAR_ID_CREDENTIAL, UsernamePasswordCredential.class);

        }
    }

}
