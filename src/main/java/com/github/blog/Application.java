package com.github.blog;

import com.github.blog.controller.*;
import com.github.blog.dto.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Set;

@ComponentScan("com.github.blog")
public class Application {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);

        UserDetailController userDetailsController = context.getBean(UserDetailController.class);
        UserController userController = context.getBean(UserController.class);
        CommentController commentController = context.getBean(CommentController.class);
        CommentReactionController commentReactionController = context.getBean(CommentReactionController.class);
        OrderController orderController = context.getBean(OrderController.class);
        PostController postController = context.getBean(PostController.class);
        PostReactionController postReactionController = context.getBean(PostReactionController.class);
        RoleController roleController = context.getBean(RoleController.class);
        TagController tagController = context.getBean(TagController.class);

        //DTO's
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

        RoleDto roleDto1 = new RoleDto();
        roleDto1.setName("admin");

        RoleDto roleDto2 = new RoleDto();
        roleDto2.setName("user");

        Set<RoleDto> roles = Set.of(roleDto1, roleDto2);
        user1.setRoles(roles);

        user1Details.setUser(user1);
        user2Details.setUser(user2);

        TagDto tagDto1 = new TagDto();
        tagDto1.setName("Java");

        TagDto tagDto2 = new TagDto();
        tagDto2.setName("Spring Boot");

        Set<TagDto> tags = Set.of(tagDto1, tagDto2);

        PostDto postDto1 = new PostDto();
        postDto1.setTitle("My first post");
        postDto1.setContent("Hello World!");
        postDto1.setTags(tags);

        PostDto postDto2 = new PostDto();
        postDto2.setTitle("Post 2");
        postDto2.setContent("World?");
        postDto2.setTags(tags);

        CommentDto commentDto1 = new CommentDto();
        commentDto1.setContent("Nice post!");
        commentDto1.setPost(postDto1);
        commentDto1.setUser(user1);

        CommentDto commentDto2 = new CommentDto();
        commentDto2.setContent("Cringe!");
        commentDto2.setPost(postDto2);
        commentDto2.setUser(user1);

        CommentReactionDto commentReactionDto1 = new CommentReactionDto();
        commentReactionDto1.setComment(commentDto1);
        commentReactionDto1.setUser(user1);
        commentReactionDto1.setReactionType("Like");

        CommentReactionDto commentReactionDto2 = new CommentReactionDto();
        commentReactionDto2.setComment(commentDto2);
        commentReactionDto2.setUser(user2);
        commentReactionDto2.setReactionType("Dislike");

        OrderDto orderDto1 = new OrderDto();
        orderDto1.setMessage("posuere felis sed");
        orderDto1.setPost(postDto1);
        orderDto1.setStatus("complete");
        orderDto1.setUser(user1);

        OrderDto orderDto2 = new OrderDto();
        orderDto2.setMessage("interdum venenatis turpis");
        orderDto2.setPost(postDto2);
        orderDto2.setStatus("cancelled");
        orderDto2.setUser(user2);

        PostReactionDto postReactionDto1 = new PostReactionDto();
        postReactionDto1.setPost(postDto1);
        postReactionDto1.setReactionType("Like");
        postReactionDto1.setUser(user1);

        PostReactionDto postReactionDto2 = new PostReactionDto();
        postReactionDto2.setPost(postDto2);
        postReactionDto2.setReactionType("Dislike");
        postReactionDto2.setUser(user2);

        //Creation
        roleController.create(roleDto1);
        roleController.create(roleDto2);

        userDetailsController.create(user1Details);
        userDetailsController.create(user2Details);

        userController.create(user1);
        userController.create(user2);

        tagController.create(tagDto1);
        tagController.create(tagDto2);

        postController.create(postDto1);
        postController.create(postDto2);

        commentController.create(commentDto1);
        commentController.create(commentDto2);

        commentReactionController.create(commentReactionDto1);
        commentReactionController.create(commentReactionDto2);

        postReactionController.create(postReactionDto1);
        postReactionController.create(postReactionDto2);

        orderController.create(orderDto1);
        orderController.create(orderDto2);

        //Testing CRUD operations

        //READ
        LOGGER.info(userController.findAll());
        LOGGER.info(userDetailsController.findAll());
        LOGGER.info(commentController.findAll());
        LOGGER.info(postController.findAll());
        LOGGER.info(orderController.findAll());
        LOGGER.info(commentReactionController.findAll());
        LOGGER.info(postReactionController.findAll());
        LOGGER.info(roleController.findAll());
        LOGGER.info(tagController.findAll());

        //DELETE
        userController.remove(2);
        userDetailsController.remove(2);
        commentController.remove(2);
        postController.remove(2);
        orderController.remove(2);
        commentReactionController.remove(2);
        postReactionController.remove(2);
        roleController.remove(2);
        tagController.remove(2);

        //UPDATE
        user1.setLogin("new_login_1");
        userController.update(1, user1);

        user1Details.setFirstname("new_name_1");
        userDetailsController.update(1, user1Details);

        commentDto1.setContent("new_content_1");
        commentController.update(1, commentDto1);


        postDto1.setContent("new_content_1");
        postController.update(1, postDto1);

        orderDto1.setMessage("new_message_1");
        orderController.update(1, orderDto1);

        commentReactionDto1.setReactionType("New_Like");
        commentReactionController.update(1, commentReactionDto1);

        postReactionDto1.setReactionType("New_Like");
        postReactionController.update(1, postReactionDto1);

        roleDto1.setName("new_name_1");
        roleController.update(1, roleDto1);

        tagDto1.setName("new_name_1");
        tagController.update(1, tagDto1);

        //READ;
        LOGGER.info(userController.findAll());
        LOGGER.info(userDetailsController.findAll());
        LOGGER.info(commentController.findAll());
        LOGGER.info(postController.findAll());
        LOGGER.info(orderController.findAll());
        LOGGER.info(commentReactionController.findAll());
        LOGGER.info(postReactionController.findAll());
        LOGGER.info(roleController.findAll());
        LOGGER.info(tagController.findAll());
    }
}