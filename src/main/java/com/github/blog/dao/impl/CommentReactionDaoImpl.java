package com.github.blog.dao.impl;

import com.github.blog.dao.CommentReactionDao;
import com.github.blog.model.CommentReaction;
import org.springframework.stereotype.Repository;

/**
 * @author Raman Haurylau
 */
@Repository
public class CommentReactionDaoImpl extends AbstractJpaDao<CommentReaction, Long> implements CommentReactionDao {
}


