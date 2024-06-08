package com.github.blog.repository;

import com.github.blog.config.DataSourceProperties;
import com.github.blog.config.PersistenceJPAConfig;
import com.github.blog.config.RepositoryTestConfig;
import com.github.blog.config.WebTestConfig;
import com.github.blog.model.Reaction;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Raman Haurylau
 */
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringJUnitConfig(classes = {WebTestConfig.class, RepositoryTestConfig.class, PersistenceJPAConfig.class, DataSourceProperties.class})
public class ReactionDaoImplTests {

    @Autowired
    private ReactionDao reactionDao;

    private static Pageable pageable;

    @BeforeAll
    public static void setUp() {
        pageable = new Pageable();
        pageable.setPageSize(Integer.MAX_VALUE);
        pageable.setPageNumber(1);
        pageable.setOrderBy("ASC");
    }

    @Test
    @Rollback
    @DisplayName("reaction dao: create")
    void create_returnsReaction_whenDataIsValid() {
        Reaction newReaction = new Reaction();
        newReaction.setName("TEST");

        Reaction createdReaction = reactionDao.create(newReaction);

        assertThat(createdReaction).isNotNull();
        assertThat(createdReaction.getId()).isNotNull();
        assertThat(createdReaction.getName()).isEqualTo("TEST");
    }

    @Test
    @DisplayName("reaction dao: find by id")
    void findById_returnsReaction_whenIdIsValid() {
        Optional<Reaction> foundReaction = reactionDao.findById(1L);

        assertThat(foundReaction).isPresent();
        assertThat(foundReaction.get().getId()).isEqualTo(1L);
        assertThat(foundReaction.get().getName()).isEqualTo("LIKE");
    }

    @Test
    @Rollback
    @DisplayName("reaction dao: update")
    void update_returnsUpdatedReaction_whenDataIsValid() {
        Optional<Reaction> optionalReaction = reactionDao.findById(1L);

        assertThat(optionalReaction).isPresent();

        Reaction updatedReaction = optionalReaction.get();
        updatedReaction.setName("Update reaction");

        updatedReaction = reactionDao.update(updatedReaction);

        assertThat(updatedReaction).isNotNull();
        assertThat(updatedReaction.getId()).isEqualTo(1L);
        assertThat(updatedReaction.getName()).isEqualTo("Update reaction");
    }

    @Test
    @Rollback
    @DisplayName("reaction dao: delete")
    void delete_deletesReaction_whenIdIsValid() {
        Optional<Reaction> optionalReaction = reactionDao.findById(1L);

        assertThat(optionalReaction).isPresent();

        Reaction deletedReaction = optionalReaction.get();

        reactionDao.delete(deletedReaction);

        assertThat(reactionDao.findAll()).isNotEmpty().hasSize(1);
    }

    @Test
    @DisplayName("reaction dao: find all with pagination")
    void findAll_withPagination_returnsPagedReactions() {
        Page<Reaction> reactionsPage = reactionDao.findAll(pageable);

        assertThat(reactionsPage.getContent()).isNotEmpty().hasSize(2);
    }
}
