package com.ticket.reservation.config;

import com.ticket.reservation.filter.CustomerFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerFilterConfig {
    @Bean
    public CustomerFilter customerFilter(){
        return new CustomerFilter();
    }

    @Bean(name = "CustomerFilterConf")
    public FilterRegistrationBean customerFilterConfig(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(customerFilter());
        filterRegistrationBean.addUrlPatterns("/api/user/*");
        filterRegistrationBean.addUrlPatterns("/api/order/*");
        filterRegistrationBean.addUrlPatterns("/api/ticket/*");
        filterRegistrationBean.setName("customerFilterConf");
        return filterRegistrationBean;
    }
}
