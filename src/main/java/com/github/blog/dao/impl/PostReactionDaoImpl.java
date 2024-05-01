package com.github.blog.dao.impl;

import com.github.blog.dao.PostReactionDao;
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

