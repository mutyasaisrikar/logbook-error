package com.example.logbookerror;

import static java.net.URLDecoder.decode;
import static java.util.Optional.ofNullable;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class SpringConfig implements WebMvcConfigurer {

    private final TestInterceptor testInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(testInterceptor);
    }

    @Slf4j
    @Component
    public static class TestInterceptor implements HandlerInterceptor {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
            String name = extractNameFromBody(request);
            log.info("Name from request Body is [{}]", name);
            return true;
        }


        private String extractNameFromBody(HttpServletRequest httpServletRequest) throws IOException {

            String requestBody = new String(IOUtils.toByteArray(httpServletRequest.getInputStream()));

            return
                ofNullable(requestBody)
                    .filter(StringUtils::isNotBlank)
                    .flatMap(body -> {
                        List<String> nameValues = JsonPath.parse(body).read("$..name");
                        return nameValues.stream().findFirst();
                    })
                    .map(token -> {
                        try {
                            return decode(token, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .orElse(null);
        }
    }
}
