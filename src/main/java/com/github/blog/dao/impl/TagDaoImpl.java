package com.github.blog.dao.impl;

import com.github.blog.dao.TagDao;
import com.github.blog.model.Tag;
import org.springframework.stereotype.Repository;

/**
 * @author Raman Haurylau
 */
@Repository
public class TagDaoImpl extends AbstractJpaDao<Tag, Long> implements TagDao {
}
