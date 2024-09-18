package com.github.blog.repository.specification;

import com.github.blog.repository.entity.PostReaction;
import com.github.blog.repository.entity.PostReaction_;
import com.github.blog.repository.entity.Post_;
import com.github.blog.repository.entity.Reaction_;
import com.github.blog.repository.entity.User;
import com.github.blog.repository.entity.User_;
import com.github.blog.repository.filter.PostReactionFilter;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

/**
 * @author Raman Haurylau
 */
public class PostReactionSpecification {

    private PostReactionSpecification() {
    }

    public static Specification<PostReaction> filterBy(PostReactionFilter postReactionFilter) {
        return Specification
                .where(hasUsername(postReactionFilter.getUsername()))
                .and(hasPostId(postReactionFilter.getPostId()))
                .and(hasReactionId(postReactionFilter.getReactionId()));
    }

    private static Specification<PostReaction> hasPostId(Long postId) {
        return (root, query, cb) -> postId == null ? cb.conjunction() : cb.equal(root.get(PostReaction_.post).get(Post_.id), postId);
    }

    private static Specification<PostReaction> hasReactionId(Long reactionId) {
        return (root, query, cb) -> reactionId == null ? cb.conjunction() : cb.equal(root.get(PostReaction_.reaction).get(Reaction_.id), reactionId);
    }

    private static Specification<PostReaction> hasUsername(String username) {
        return (root, query, cb) -> {
            if (!ObjectUtils.isEmpty(username)) {
                Join<PostReaction, User> user = root.join(PostReaction_.user, JoinType.LEFT);
                return cb.like(cb.lower(user.get(User_.username)), username.toLowerCase().concat("%"));
            } else {
                return cb.conjunction();
            }
        };
    }
}
