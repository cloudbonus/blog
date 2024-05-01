package com.github.blog.repository.impl;

import com.github.blog.repository.PostReactionDao;
import com.github.blog.model.PostReaction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Raman Haurylau
 */
@Repository
@Transactional
public class PostReactionDaoImpl extends AbstractJpaDao<PostReaction, Long> implements PostReactionDao {
}

