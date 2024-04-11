package com.github.blog;

import com.github.blog.controller.UserController;
import com.github.blog.controller.UserDetailsController;
import com.github.blog.dto.UserDetailsDto;
import com.github.blog.dto.UserDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.AbstractApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Log4j2
@ComponentScan("com.github.blog")
public class Application {

    public static void main(String[] args) {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(Application.class);

        UserDetailsController userDetailsController = context.getBean(UserDetailsController.class);
        UserController userController = context.getBean(UserController.class);

        //DTO
        UserDto user1 = new UserDto();
        user1.setLogin("kvossing0");
        user1.setPassword("123");
        user1.setEmail("vpenzer0@icio.us");

        UserDto user2 = new UserDto();
        user2.setLogin("gmaccook1");
        user2.setPassword("lY3<OP4Y");
        user2.setEmail("rpucker1@statcounter.com");

        UserDetailsDto user1Details = new UserDetailsDto();
        user1Details.setFirstname("Karl");
        user1Details.setSurname("Doe");
        user1Details.setUniversityName("Harvard University");
        user1Details.setMajorName("Computer Science");
        user1Details.setCompanyName("Google");
        user1Details.setJobTitle("Software Engineer");

        UserDetailsDto user2Details = new UserDetailsDto();
        user2Details.setFirstname("Alice");
        user2Details.setSurname("Johnson");
        user2Details.setUniversityName("MIT");
        user2Details.setMajorName("Artificial Intelligence");
        user2Details.setCompanyName("Facebook");
        user2Details.setJobTitle("AI Researcher");

        user1Details.setUser(user1);
        user2Details.setUser(user2);

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.execute(() -> userController.create(user1));

        executorService.execute(() -> userController.create(user2));

        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

        executorService = Executors.newFixedThreadPool(2);


        executorService.execute(() -> userDetailsController.create(user1Details));

        executorService.execute(() -> userDetailsController.create(user2Details));

        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

        executorService = Executors.newFixedThreadPool(2);


        executorService.execute(() -> log.info(userController.findAllByUniversity(user2Details)));

        executorService.execute(() -> {
            log.info(userController.findAllByUniversity(user2Details));
            log.info(userController.findAllByRole("ROLE_USER"));
        });

        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

        log.info(userController.findAllByUniversity(user2Details));

        context.close();
    }
}