package com.nix.reference.spring.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * A Spring Boot project which showcases an end-to-end built project
 *
 * @author bogdan.solga
 */
@SpringBootApplication
public class SpringReferenceProject {

    public static void main(String[] args) {
        final SpringApplication application = new SpringApplication(SpringReferenceProject.class);
        application.setAdditionalProfiles(Profiles.DEV);
        application.run(args);
    }
}
