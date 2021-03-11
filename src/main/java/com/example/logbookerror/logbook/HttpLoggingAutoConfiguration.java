package com.example.logbookerror.logbook;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.servlet.LogbookFilter;

@Slf4j
@Configuration
@Import(CustomLoggingSink.class)
public class HttpLoggingAutoConfiguration {

    @Bean
    public FilterRegistrationBean<LogbookFilter> logbookFilter(CustomLoggingSink customLoggingSink) {

        Logbook logbook = Logbook.builder()
                .sink(customLoggingSink)
                .build();

        LogbookFilter logbookFilter =  new LogbookFilter(logbook);

        FilterRegistrationBean<LogbookFilter> filterRegistration = new FilterRegistrationBean<>();
        filterRegistration.setFilter(logbookFilter);
        filterRegistration.setOrder(2);
        return filterRegistration;
    }
}



