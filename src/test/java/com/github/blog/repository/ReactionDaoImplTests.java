package com.github.blog.repository;

import com.github.blog.config.ContainerConfig;
import com.github.blog.model.Reaction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Raman Haurylau
 */
@Transactional
@SpringBootTest(classes = ContainerConfig.class)
public class ReactionDaoImplTests {

    @Autowired
    private ReactionRepository reactionRepository;

    private static Pageable pageable;

    @BeforeAll
    public static void setUp() {
        pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by("id").ascending());
    }

    @Test
    @Rollback
    @DisplayName("reaction dao: create")
    void create_returnsReaction_whenDataIsValid() {
        Reaction newReaction = new Reaction();
        newReaction.setName("TEST");

        Reaction createdReaction = reactionRepository.save(newReaction);

        assertThat(createdReaction).isNotNull();
        assertThat(createdReaction.getId()).isNotNull();
        assertThat(createdReaction.getName()).isEqualTo("TEST");
    }

    @Test
    @DisplayName("reaction dao: find by id")
    void findById_returnsReaction_whenIdIsValid() {
        Optional<Reaction> foundReaction = reactionRepository.findById(1L);

        assertThat(foundReaction).isPresent();
        assertThat(foundReaction.get().getId()).isEqualTo(1L);
        assertThat(foundReaction.get().getName()).isEqualTo("LIKE");
    }

    @Test
    @Rollback
    @DisplayName("reaction dao: update")
    void update_returnsUpdatedReaction_whenDataIsValid() {
        Optional<Reaction> optionalReaction = reactionRepository.findById(1L);

        assertThat(optionalReaction).isPresent();

        Reaction updatedReaction = optionalReaction.get();
        updatedReaction.setName("Update reaction");

        updatedReaction = reactionRepository.save(updatedReaction);

        assertThat(updatedReaction).isNotNull();
        assertThat(updatedReaction.getId()).isEqualTo(1L);
        assertThat(updatedReaction.getName()).isEqualTo("Update reaction");
    }

    @Test
    @Rollback
    @DisplayName("reaction dao: delete")
    void delete_deletesReaction_whenIdIsValid() {
        Optional<Reaction> optionalReaction = reactionRepository.findById(1L);

        assertThat(optionalReaction).isPresent();

        Reaction deletedReaction = optionalReaction.get();

        reactionRepository.delete(deletedReaction);

        assertThat(reactionRepository.findAll()).isNotEmpty().hasSize(1);
    }

    @Test
    @DisplayName("reaction dao: find all with pagination")
    void findAll_withPagination_returnsPagedReactions() {
        Page<Reaction> reactionsPage = reactionRepository.findAll(pageable);

        assertThat(reactionsPage.getContent()).isNotEmpty().hasSize(2);
    }
}
