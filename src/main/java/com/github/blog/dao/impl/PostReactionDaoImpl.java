package com.github.blog.dao.impl;

import com.github.blog.dao.PostReactionDao;
import com.github.blog.model.PostReaction;
import org.springframework.stereotype.Repository;

/**
 * @author Raman Haurylau
 */
@Repository
public class PostReactionDaoImpl extends AbstractJpaDao<PostReaction, Long> implements PostReactionDao {

}

