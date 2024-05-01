package com.github.blog.repository.impl;

import com.github.blog.repository.CommentReactionDao;
import com.github.blog.model.CommentReaction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Raman Haurylau
 */
@Repository
@Transactional
public class CommentReactionDaoImpl extends AbstractJpaDao<CommentReaction, Long> implements CommentReactionDao {
}


