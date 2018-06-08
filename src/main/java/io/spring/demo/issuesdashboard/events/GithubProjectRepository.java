package io.spring.demo.issuesdashboard.events;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Mono;

public interface GithubProjectRepository extends ReactiveMongoRepository<GithubProject, Long> {

	Mono<GithubProject> findByRepoName(String repoName);
}
