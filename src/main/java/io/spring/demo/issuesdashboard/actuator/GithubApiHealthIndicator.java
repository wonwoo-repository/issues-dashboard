package io.spring.demo.issuesdashboard.actuator;

import org.springframework.boot.actuate.health.AbstractReactiveHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

import io.spring.demo.issuesdashboard.github.GithubClient;
import io.spring.demo.issuesdashboard.github.RepositoryEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class GithubApiHealthIndicator extends AbstractReactiveHealthIndicator {

	private final GithubClient githubClient;

	public GithubApiHealthIndicator(GithubClient githubClient) {
		this.githubClient = githubClient;
	}

	//TODO

    @Override
    protected Mono<Health> doHealthCheck(Health.Builder builder) {
        Flux<RepositoryEvent> events = githubClient.fetchEvents("spring-projects", "spring-boot");
        return events.map(repositoryEvent -> up(builder, repositoryEvent)).next()
                .onErrorResume(ex -> Mono.just(new Health.Builder().down(ex).build()));
    }

    private Health up(Health.Builder builder, RepositoryEvent repositoryEvent) {
        return builder.up().withDetail("actor", repositoryEvent.getActor()).build();
    }
}
