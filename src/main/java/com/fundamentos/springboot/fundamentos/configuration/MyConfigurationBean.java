package com.fundamentos.springboot.fundamentos.configuration;

import com.fundamentos.springboot.fundamentos.bean.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfigurationBean {
    @Bean
    public MyBean beanOperation(){
        return new MyBeanImplement();
    }

    @Bean
    public MyOperation beanOperationOperationWithDependency(){
        return new MyOperationImplement();
    }

    @Bean
    public MyBeanWithDependency beanOperationOperation(MyOperation myOperation){
        return new MyBeanWithDependencyImplement(myOperation);
    }
}
