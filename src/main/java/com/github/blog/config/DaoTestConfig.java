package com.github.blog.config;

import com.github.blog.dao.CommentDao;
import com.github.blog.dao.PostDao;
import com.github.blog.dao.RoleDao;
import com.github.blog.dao.TagDao;
import com.github.blog.dao.UserDao;
import com.github.blog.dao.UserDetailDao;
import com.github.blog.dao.impl.CommentDaoImpl;
import com.github.blog.dao.impl.PostDaoImpl;
import com.github.blog.dao.impl.RoleDaoImpl;
import com.github.blog.dao.impl.TagDaoImpl;
import com.github.blog.dao.impl.UserDaoImpl;
import com.github.blog.dao.impl.UserDetailDaoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Raman Haurylau
 */
@Configuration
public class DaoTestConfig {
    @Bean
    public UserDao userDao() {
        return new UserDaoImpl();
    }

    @Bean
    public RoleDao roleDao() {
        return new RoleDaoImpl();
    }

    @Bean
    public UserDetailDao userDetailDao() {
        return new UserDetailDaoImpl();
    }

    @Bean
    public PostDao postDao() {
        return new PostDaoImpl();
    }

    @Bean
    public CommentDao commentDao() {
        return new CommentDaoImpl();
    }

    @Bean
    public TagDao tagDao() {
        return new TagDaoImpl();
    }
}
