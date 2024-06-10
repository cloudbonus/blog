package com.github.blog.repository.specification;

import com.github.blog.model.CommentReaction;
import com.github.blog.model.CommentReaction_;
import com.github.blog.model.Comment_;
import com.github.blog.model.Reaction_;
import com.github.blog.model.User;
import com.github.blog.model.User_;
import com.github.blog.repository.filter.CommentReactionFilter;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

/**
 * @author Raman Haurylau
 */
public class CommentReactionSpecification {

    private CommentReactionSpecification() {
    }

    public static Specification<CommentReaction> filterBy(CommentReactionFilter commentReactionFilter) {
        return Specification
                .where(hasUsername(commentReactionFilter.getUsername()))
                .and(hasCommentId(commentReactionFilter.getCommentId()))
                .and(hasReactionId(commentReactionFilter.getReactionId()));
    }

    private static Specification<CommentReaction> hasCommentId(Long commentId) {
        return (root, query, cb) -> commentId == null ? cb.conjunction() : cb.equal(root.get(CommentReaction_.comment).get(Comment_.id), commentId);
    }

    private static Specification<CommentReaction> hasReactionId(Long reactionId) {
        return (root, query, cb) -> reactionId == null ? cb.conjunction() : cb.equal(root.get(CommentReaction_.reaction).get(Reaction_.id), reactionId);
    }

    private static Specification<CommentReaction> hasUsername(String username) {
        return (root, query, cb) -> {
            if (!ObjectUtils.isEmpty(username)) {
                Join<CommentReaction, User> user = root.join(CommentReaction_.user, JoinType.LEFT);
                return cb.like(cb.lower(user.get(User_.username)), username.toLowerCase().concat("%"));
            } else {
                return cb.conjunction();
            }
        };
    }
}
