package io.spring.demo.issuesdashboard.github;

import java.nio.charset.StandardCharsets;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.*;

import io.micrometer.core.instrument.MeterRegistry;
import io.spring.demo.issuesdashboard.GithubProperties;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class GithubClient {

    private final WebClient webClient;
    private static final String EVENT_ISSUES_BASE_URL = "https://api.github.com";

    public GithubClient(WebClient.Builder client, GithubProperties properties, MeterRegistry meterRegistry) {
        this.webClient = client.filter(new GithubAppTokenInterceptor(properties.getToken()))
                .baseUrl(EVENT_ISSUES_BASE_URL)
                .build();
    }

    public Flux<RepositoryEvent> fetchEvents(String orgName, String repoName) {
        return this.webClient.get().uri("/repos/{owner}/{repo}/issues/events", orgName, repoName)
                .retrieve()
                .bodyToFlux(RepositoryEvent.class);
    }

    @Cacheable("events")
    public Flux<RepositoryEvent> fetchEventsList(String orgName, String repoName) {
        return fetchEvents(orgName, repoName);
    }

    private static class GithubAppTokenInterceptor implements ExchangeFilterFunction {
        private final String token;

        GithubAppTokenInterceptor(String token) {
            this.token = token;
        }

        @Override
        public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
            if (StringUtils.hasText(this.token)) {
                byte[] basicAuthValue = this.token.getBytes(StandardCharsets.UTF_8);
                request.headers().set(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(basicAuthValue));
            }
            return next.exchange(request);
        }
    }

    //TODO

//    private static class MetricsInterceptor implements ExchangeFilterFunction {
//
//        private final AtomicInteger gauge;
//
//        public MetricsInterceptor(MeterRegistry meterRegistry) {
//            this.gauge = meterRegistry.gauge("github.ratelimit.remaining", new AtomicInteger(0));
//        }
        //
        //		@Override
        //		public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes,
        //				ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        //			ClientHttpResponse response = clientHttpRequestExecution.execute(httpRequest, bytes);
        //			this.gauge.set(Integer.parseInt(response.getHeaders().getFirst("X-RateLimit-Remaining")));
        //			return response;
        //		}

//        @Override
//        public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
//            Mono<ClientResponse> exchange = next.exchange(request)
//                    .flatMap(clientResponse -> this.gauge.set(Integer.parseInt(clientResponse.headers().header("X-RateLimit-Remaining").get(0))))
//            return null;
//        }
    }

    //	private static class GithubAppTokenInterceptor implements ClientHttpRequestInterceptor {
    //
    //		private final String token;
    //
    //		GithubAppTokenInterceptor(String token) {
    //			this.token = token;
    //		}
    //
    //		@Override
    //		public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes,
    //				ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
    //			if (StringUtils.hasText(this.token)) {
    //				byte[] basicAuthValue = this.token.getBytes(StandardCharsets.UTF_8);
    //				httpRequest.getHeaders().set(HttpHeaders.AUTHORIZATION,
    //						"Basic " + Base64Utils.encodeToString(basicAuthValue));
    //			}
    //			return clientHttpRequestExecution.execute(httpRequest, bytes);
    //		}
    //
    //	}
    //
    //	private static class MetricsInterceptor implements ClientHttpRequestInterceptor {
    //
    //		private final AtomicInteger gauge;
    //
    //		public MetricsInterceptor(MeterRegistry meterRegistry) {
    //			this.gauge = meterRegistry.gauge("github.ratelimit.remaining", new AtomicInteger(0));
    //		}
    //
    //		@Override
    //		public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes,
    //				ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
    //			ClientHttpResponse response = clientHttpRequestExecution.execute(httpRequest, bytes);
    //			this.gauge.set(Integer.parseInt(response.getHeaders().getFirst("X-RateLimit-Remaining")));
    //			return response;
    //		}
