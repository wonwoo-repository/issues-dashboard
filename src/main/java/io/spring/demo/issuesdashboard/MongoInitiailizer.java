package io.spring.demo.issuesdashboard;

import java.util.Arrays;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.stereotype.Component;

import io.spring.demo.issuesdashboard.events.GithubProject;
import io.spring.demo.issuesdashboard.events.GithubProjectRepository;
import io.spring.demo.issuesdashboard.user.User;
import io.spring.demo.issuesdashboard.user.UserRepository;

@Component
public class MongoInitiailizer implements SmartInitializingSingleton {

    private final GithubProjectRepository githubProjectRepository;
    private final UserRepository userRepository;

    public MongoInitiailizer(GithubProjectRepository githubProjectRepository, UserRepository userRepository) {
        this.githubProjectRepository = githubProjectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void afterSingletonsInstantiated() {

        User admin = new User(1L, "admin@test.com", "admin", "wonwoo", "lee");
        User user = new User(2L, "user@test.com", "password", "kevin", "kim");

        this.userRepository.deleteAll()
                .thenMany(this.userRepository.saveAll(Arrays.asList(admin, user)))
                .subscribe(null, null, () -> this.userRepository.findAll().subscribe(System.out::println));


        GithubProject springBoot = new GithubProject("spring-projects", "spring-boot");
        GithubProject springInitializr = new GithubProject("spring-io", "initializr");
        GithubProject springSagan = new GithubProject("spring-io", "sagan");

        this.githubProjectRepository.deleteAll()
                .thenMany(this.githubProjectRepository.saveAll(Arrays.asList(springBoot, springInitializr, springSagan)))
                        .subscribe(null, null,
                                () -> this.githubProjectRepository.findAll().subscribe(System.out::println));
    }
}
