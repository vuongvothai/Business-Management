package com.businessmanagement.market.service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class CoffeeMarketPriceFetcher {
    private static final String SOURCE = "https://giacaphe.com/gia-ca-phe-noi-dia/";
    private static final Pattern AVERAGE_PRICE = Pattern.compile("(?:trung bình|trung b\\u00ecnh).*?([0-9][0-9.,]*)\\s*(?:vn\\s*đ|đ|₫)\\s*/?\\s*kg", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.DOTALL);
    private final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(8)).build();

    public BigDecimal fetchAveragePrice() {
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(SOURCE))
                    .timeout(Duration.ofSeconds(12))
                    .header("User-Agent", "BusinessManagement/1.0")
                    .GET().build();
            String html = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            Matcher matcher = AVERAGE_PRICE.matcher(html.replaceAll("<[^>]+>", " ").replaceAll("\\s+", " ").replace("&nbsp;", " "));
            if (!matcher.find()) throw new IllegalStateException("Average price not found");
            return new BigDecimal(matcher.group(1).replace(".", "").replace(",", "."));
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to fetch coffee market price", exception);
        }
    }
}
