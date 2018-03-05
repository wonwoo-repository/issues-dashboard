package io.spring.demo.issuesdashboard.events;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;

import io.spring.demo.issuesdashboard.github.GithubClient;
import io.spring.demo.issuesdashboard.github.RepositoryEvent;
import reactor.core.publisher.Flux;

@Controller
public class EventsController {

    private final GithubClient githubClient;

    private final GithubProjectRepository repository;

    public EventsController(GithubClient githubClient, GithubProjectRepository repository) {
        this.githubClient = githubClient;
        this.repository = repository;
    }

    @GetMapping("/events/{projectName}")
    @ResponseBody
    public Flux<RepositoryEvent> fetchEvents(@PathVariable String projectName) {

        return this.repository.findByRepoName(projectName)
                .flatMapMany(project ->
                        this.githubClient.fetchEvents(project.getOrgName(), project.getRepoName()));
    }


    //TODO
    @GetMapping("/")
    public Rendering dashboard() {
        Flux<DashboardEntry> entries = this.repository.findAll()
                .map(githubProject -> new DashboardEntry(githubProject,
                        githubClient.fetchEventsList(githubProject.getOrgName(), githubProject.getRepoName()).collectList().block()));
        return Rendering.view("dashboard")
                .modelAttribute("entries", entries)
                .build();
    }

    @GetMapping("/admin")
    public Rendering admin() {
        return Rendering.view("admin")
                .modelAttribute("projects", repository.findAll())
                .build();
    }

    @PostMapping("/admin")
    public Rendering createGithubProject(@ModelAttribute GithubProject githubProject) {
        return Rendering.view("redirect:/")
                .modelAttribute("entries", this.repository.save(githubProject))
                .build();
    }
}
