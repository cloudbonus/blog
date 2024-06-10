package com.github.blog.repository.specification;

import com.github.blog.model.Post;
import com.github.blog.model.Post_;
import com.github.blog.model.Tag;
import com.github.blog.model.Tag_;
import com.github.blog.repository.filter.TagFilter;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

/**
 * @author Raman Haurylau
 */
public class TagSpecification {

    private TagSpecification() {
    }

    public static Specification<Tag> filterBy(TagFilter tagFilter) {
        return Specification
                .where(hasPostId(tagFilter.getPostId()));
    }

    private static Specification<Tag> hasPostId(Long postId) {
        return (root, query, cb) -> {
            if (!ObjectUtils.isEmpty(postId)) {
                Join<Tag, Post> post = root.join(Tag_.posts, JoinType.LEFT);
                return cb.equal(post.get(Post_.id), postId);
            } else {
                return cb.conjunction();
            }
        };
    }
}
