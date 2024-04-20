package com.github.blog;

import com.github.blog.config.AppConfig;
import com.github.blog.controller.UserController;
import com.github.blog.controller.UserDetailController;
import com.github.blog.controller.mapper.JsonMapper;
import com.github.blog.dto.UserDetailDto;
import com.github.blog.dto.UserDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.time.OffsetDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Log4j2
public class Application {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserDetailController userDetailsController = context.getBean(UserDetailController.class);
        UserController userController = context.getBean(UserController.class);
        JsonMapper jsonMapper = context.getBean(JsonMapper.class);

        UserDto user1 = new UserDto();
        user1.setLogin("kvossing0");
        user1.setPassword("123");
        user1.setEmail("vpenzer0@icio.us");
        user1.setCreatedAt(OffsetDateTime.now());
        user1.setLastLogin(OffsetDateTime.now());

        UserDto user2 = new UserDto();
        user2.setLogin("gmaccook1");
        user2.setPassword("lY3<OP4Y");
        user2.setEmail("rpucker1@statcounter.com");
        user2.setCreatedAt(OffsetDateTime.now());
        user2.setLastLogin(OffsetDateTime.now());

        UserDto finalUser1 = user1;
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Future<String> future1 = executorService.submit(() -> userController.create(finalUser1));

        UserDto finalUser2 = user2;
        Future<String> future2 = executorService.submit(() -> userController.create(finalUser2));

        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

        user1 = jsonMapper.convertToDto(future1.get(), UserDto.class);
        user2 = jsonMapper.convertToDto(future2.get(), UserDto.class);

        UserDetailDto user1Details = new UserDetailDto();
        user1Details.setId(user1.getId());
        user1Details.setFirstname("Karl");
        user1Details.setSurname("Doe");
        user1Details.setUniversityName("Harvard University");
        user1Details.setMajorName("Computer Science");
        user1Details.setCompanyName("Google");
        user1Details.setJobTitle("Software Engineer");

        UserDetailDto user2Details = new UserDetailDto();
        user2Details.setId(user2.getId());
        user2Details.setFirstname("Alice");
        user2Details.setSurname("Johnson");
        user2Details.setUniversityName("MIT");
        user2Details.setMajorName("Artificial Intelligence");
        user2Details.setCompanyName("Facebook");
        user2Details.setJobTitle("AI Researcher");

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
        log.info(userController.findAllByRole("ROLE_USER"));

        executorService = Executors.newFixedThreadPool(2);

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