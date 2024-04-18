package com.github.blog;

import com.github.blog.config.AppConfig;
import com.github.blog.controller.UserController;
import com.github.blog.dto.UserDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.time.OffsetDateTime;

@Log4j2
public class Application {

    public static void main(String[] args) {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserController userController = context.getBean(UserController.class);
        UserDto user = new UserDto("kvossing0",
                "123",
                "vpenzer0@icio.us",
                OffsetDateTime.now(),
                OffsetDateTime.now());

        userController.create(user);
//        TagController tagController = context.getBean(TagController.class);
//        TagDto tagDto = new TagDto("check");
//        tagController.create(tagDto);
//        log.info(tagController.findAll());
        context.close();
    }
}