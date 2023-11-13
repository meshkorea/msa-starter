package {{packageName}}.config.tracing;

import static {{packageName}}.config.Constants.LogKey.*;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.startsWithIgnoreCase;
import static org.zalando.logbook.core.HeaderFilters.authorization;

import {{packageName}}.support.JsonUtils;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.*;
import org.zalando.logbook.core.*;
import org.zalando.logbook.json.PrettyPrintingJsonBodyFilter;

@Configuration
public class LogBookConfiguration {

  private static final Pattern JSON_PATTERN = Pattern.compile("application\\/.*json.*");

  @Bean
  public Logbook logBook(Tracer tracer) {
    return Logbook.builder()
        .condition(new CustomCondition())
        .headerFilter(authorization())
        .queryFilter(new CustomQueryFilter())
        .bodyFilter(new PrettyPrintingJsonBodyFilter())
        .correlationId(new CustomCorrelationId(tracer))
        .sink(new DefaultSink(new CustomHttpLogFormatter(), new CustomHttpLogWriter()))
        .build();
  }

  public static class CustomCondition implements Predicate<HttpRequest> {

    @Override
    public boolean test(HttpRequest httpRequest) {
      if (isRawContentRequest(httpRequest)) {
        return false;
      }
      if (isPreflightRequest(httpRequest)) {
        return false;
      }
      if (isBlacklistedPath(httpRequest)) {
        return false;
      }

      return true;
    }

    private boolean isRawContentRequest(HttpRequest request) {
      final String contentType = request.getContentType();
      return containsIgnoreCase(contentType, "octet-stream");
    }

    private boolean isPreflightRequest(HttpRequest request) {
      final String method = request.getMethod();
      return containsIgnoreCase(method, "OPTIONS") || containsIgnoreCase(method, "HEAD");
    }

    private boolean isBlacklistedPath(HttpRequest request) {
      final String path = request.getPath();
      return startsWithIgnoreCase(path, "/management/");
    }
  }

  public static class CustomQueryFilter implements QueryFilter {
    @Override
    public String filter(String query) {
      return URLDecoder.decode(query, Charset.defaultCharset());
    }
  }

  @RequiredArgsConstructor
  public static class CustomCorrelationId implements CorrelationId {

    private final Tracer tracer;

    @Override
    public String generate(HttpRequest request) {
      final Span currentSpan = tracer.currentSpan();
      if (currentSpan == null) {
        return new DefaultCorrelationId().generate(request);
      }

      return currentSpan.context().traceId();
    }
  }

  public static class CustomHttpLogFormatter implements HttpLogFormatter {

    @Override
    public String format(Precorrelation precorrelation, HttpRequest request) throws IOException {
      final Map<String, Object> reqLog = new HashMap<>();
      reqLog.put(ENDPOINT, request.getMethod() + " " + request.getRequestUri());

      if (isJsonRequest(request)) {
        reqLog.put(BODY, JsonUtils.convertStringToJsonNode(request.getBodyAsString()));
      }

      return "HTTP Request\n" + JsonUtils.convertObjectToPrettyString(reqLog);
    }

    @Override
    public String format(Correlation correlation, HttpResponse response) throws IOException {
      final Map<String, Object> resLog = new HashMap<>();
      resLog.put(STATUS, response.getStatus());

      if (isJsonResponse(response)) {
        resLog.put(BODY, JsonUtils.convertStringToJsonNode(response.getBodyAsString()));
      }

      return "HTTP Response\n" + JsonUtils.convertObjectToPrettyString(resLog);
    }

    private boolean isJsonRequest(HttpRequest request) {
      final String contentType = request.getContentType();
      return contentType != null && JSON_PATTERN.matcher(contentType).matches();
    }

    private boolean isJsonResponse(HttpResponse response) {
      final String contentType = response.getContentType();
      return contentType != null && JSON_PATTERN.matcher(contentType).matches();
    }
  }

  public static class CustomHttpLogWriter implements HttpLogWriter {

    private Logger log = LoggerFactory.getLogger(HTTP_INBOUND_LOGGER);

    @Override
    public void write(Precorrelation precorrelation, String request) {
      log.info(request);
    }

    @Override
    public void write(Correlation correlation, String response) {
      log.info(response);
    }
  }
}
