package com.example.logbookerror.logbook;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.zalando.logbook.Correlation;
import org.zalando.logbook.HttpRequest;
import org.zalando.logbook.HttpResponse;
import org.zalando.logbook.Precorrelation;
import org.zalando.logbook.Sink;

@Slf4j
@Component
public class CustomLoggingSink implements Sink {

    private static final Collector<CharSequence, ?, String> JOINER = Collectors.joining(" ");

    @Override
    public void write(Precorrelation precorrelation, HttpRequest request) {
        //nothing to do
    }

    @Override
    public void write(Correlation correlation, HttpRequest request, HttpResponse response) throws IOException {

        logExchange(correlation, request, response);
    }

    private void logExchange(Correlation correlation,
                             HttpRequest request,
                             HttpResponse response) throws IOException {

        Map<String, Object> exchangeItems = new LinkedHashMap<>();
        exchangeItems.put("endpoint", request.getPath());
        exchangeItems.put("method", request.getMethod());
        exchangeItems.put("responseTime", correlation.getDuration().toMillis());
        exchangeItems.put("headers", request.getHeaders());
        exchangeItems.put("queryParameters", request.getQuery());
        exchangeItems.put("requestBody", request.getBodyAsString());
        exchangeItems.put("responseStatus", response.getStatus());
        exchangeItems.put("responseHeaders", response.getHeaders());
        exchangeItems.put("responseBody", response.getBodyAsString());

        log.info(getLogText(exchangeItems));
    }


    private static String getLogText(Map<String, Object> data) {
        return data.entrySet().stream()
            .map(entry -> entry.getKey() + "=" + trimToEmpty(entry.getValue()))
            .collect(JOINER);
    }

    private static String trimToEmpty(Object object) {
        return object == null ? "" : object.toString().trim();
    }
}
