package com.github.blog.repository;

import com.github.blog.config.DataSourceProperties;
import com.github.blog.config.PersistenceJPAConfig;
import com.github.blog.config.RepositoryTestConfig;
import com.github.blog.config.WebTestConfig;
import com.github.blog.model.Tag;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.TagFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
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
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS, scripts = "/db/clean-test-data.sql")
public class TagDaoImplTests {

    @Autowired
    private TagDao tagDao;

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
    @DisplayName("tag dao: create")
    void create_returnsTag_whenDataIsValid() {
        Tag newTag = new Tag();
        newTag.setName("New Tag");

        Tag createdTag = tagDao.create(newTag);

        assertThat(createdTag).isNotNull();
        assertThat(createdTag.getId()).isNotNull();
        assertThat(createdTag.getName()).isEqualTo("New Tag");
    }

    @Test
    @DisplayName("tag dao: find by id")
    void findById_returnsTag_whenIdIsValid() {
        Optional<Tag> foundTag = tagDao.findById(4L);

        assertThat(foundTag).isPresent();
        assertThat(foundTag.get().getId()).isEqualTo(4L);
        assertThat(foundTag.get().getName()).isEqualTo("NEWS");
    }

    @Test
    @Rollback
    @DisplayName("tag dao: update")
    void update_returnsUpdatedTag_whenDataIsValid() {
        Optional<Tag> optionalTag = tagDao.findById(4L);

        assertThat(optionalTag).isPresent();

        Tag updatedTag = optionalTag.get();
        updatedTag.setName("Updated Tag");

        updatedTag = tagDao.update(updatedTag);

        assertThat(updatedTag).isNotNull();
        assertThat(updatedTag.getId()).isEqualTo(4L);
        assertThat(updatedTag.getName()).isEqualTo("Updated Tag");
    }

    @Test
    @Rollback
    @DisplayName("tag dao: delete")
    void delete_deletesTag_whenIdIsValid() {
        Optional<Tag> optionalTag = tagDao.findById(1L);

        assertThat(optionalTag).isPresent();

        Tag deletedTag = optionalTag.get();

        tagDao.delete(deletedTag);

        assertThat(tagDao.findAll()).isNotEmpty().hasSize(3);
    }

    @Test
    @DisplayName("tag dao: find all with filter and pagination")
    @Sql({"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-post-table.sql", "/db/insert-test-data-into-post_tag-table.sql"})
    void findAll_withFilterAndPagination_returnsFilteredAndPagedTags() {
        TagFilter filter = new TagFilter();
        filter.setPostId(1L);

        Page<Tag> tagsPage = tagDao.findAll(filter, pageable);

        assertThat(tagsPage.getContent()).isNotEmpty().hasSize(1);
    }
}
