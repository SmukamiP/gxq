package com.hengyunsoft.platform.config;

import com.hengyunsoft.base.id.IdGenerate;
import com.hengyunsoft.base.id.SnowflakeIDGenerate;
import com.hengyunsoft.commons.handler.GlobalExceptionHandler;
import com.hengyunsoft.platform.commons.feign.DateFormatRegister;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import com.hengyunsoft.utils.SpringUtil;
import com.hengyunsoft.validator.extract.DefaultConstraintExtractImpl;
import com.hengyunsoft.validator.extract.IConstraintExtract;

import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.context.ApplicationContext;
/**
 * @author tyh
 * @createTime 2017-12-15 14:42
 */
@Configuration
public class PipingConfiguration {
    /**
     * 全局异常处理
     *
     * @return
     */
    @Bean
    public GlobalExceptionHandler getGlobalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    public IdGenerate getIdGenerate(@Value("${id-generator.machine-code:1}") Long machineCode) {
        return new SnowflakeIDGenerate(machineCode);
    }

    /*************************验证架构方面的配置start
     * @throws Exception ******************************/
    @Bean
    public IConstraintExtract constraintExtract() throws Exception {
    	
    	return new DefaultConstraintExtractImpl(validator());
    }
    
    @Bean
    public Validator validator() {
    	
		return Validation.buildDefaultValidatorFactory().getValidator();
	}
    /*************************验证架构方面的配置end******************************/
    
    @Bean
    public SpringUtil springUtil(ApplicationContext applicationContext) {
    	
    	SpringUtil.setApplicationContext(applicationContext);
    	return new SpringUtil();
    }
    
    @Bean
    public DateFormatRegister dateFormatRegister() {
    	
    	return new DateFormatRegister();
    }
    
}
