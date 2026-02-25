package com.community.java_community_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing  // 启用JPA审计功能，支持@CreatedDate和@LastModifiedDate
public class JavaCommunityBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaCommunityBackendApplication.class, args);
	}

}
