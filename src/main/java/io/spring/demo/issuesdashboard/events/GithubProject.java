package io.spring.demo.issuesdashboard.events;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class GithubProject implements Serializable {

	@Id
	private String id;

	private String orgName;

	private String repoName;

	public GithubProject() {
	}

	public GithubProject(String orgName, String repoName) {
		this.orgName = orgName;
		this.repoName = repoName;
	}

	public String getId() {
		return id;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getRepoName() {
		return repoName;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	@Override
	public String toString() {
		return "GithubProject{" +
				"id=" + id +
				", orgName='" + orgName + '\'' +
				", repoName='" + repoName + '\'' +
				'}';
	}
}
