package com.simpleir.wiki;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan(basePackages = { "com.simpleir" }, excludeFilters = {
        @Filter(type = FilterType.ASSIGNABLE_TYPE,
                value = {
                    Configurations.class
                })})
@EnableAspectJAutoProxy
@PropertySource("classpath:application.properties")
public class Configurations
{
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer()
    {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
