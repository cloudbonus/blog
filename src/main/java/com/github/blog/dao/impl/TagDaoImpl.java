package com.github.blog.dao.impl;

import com.github.blog.dao.TagDao;
import com.github.blog.model.Tag;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Raman Haurylau
 */
@Repository
@Transactional
public class TagDaoImpl extends AbstractJpaDao<Tag, Long> implements TagDao {
}
