package com.github.blog.repository;

import com.github.blog.config.ContainerConfig;
import com.github.blog.model.Tag;
import com.github.blog.repository.filter.TagFilter;
import com.github.blog.repository.specification.TagSpecification;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Raman Haurylau
 */
@Transactional
@SpringBootTest(classes = ContainerConfig.class)
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS, scripts = "/db/clean-test-data.sql")
public class TagDaoImplTests {

    @Autowired
    private TagRepository tagRepository;

    private static Pageable pageable;

    @BeforeAll
    public static void setUp() {
        pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by("id").ascending());
    }

    @Test
    @Rollback
    @DisplayName("tag dao: create")
    void create_returnsTag_whenDataIsValid() {
        Tag newTag = new Tag();
        newTag.setName("New Tag");

        Tag createdTag = tagRepository.save(newTag);

        assertThat(createdTag).isNotNull();
        assertThat(createdTag.getId()).isNotNull();
        assertThat(createdTag.getName()).isEqualTo("New Tag");
    }

    @Test
    @DisplayName("tag dao: find by id")
    void findById_returnsTag_whenIdIsValid() {
        Optional<Tag> foundTag = tagRepository.findById(4L);

        assertThat(foundTag).isPresent();
        assertThat(foundTag.get().getId()).isEqualTo(4L);
        assertThat(foundTag.get().getName()).isEqualTo("NEWS");
    }

    @Test
    @Rollback
    @DisplayName("tag dao: update")
    void update_returnsUpdatedTag_whenDataIsValid() {
        Optional<Tag> optionalTag = tagRepository.findById(4L);

        assertThat(optionalTag).isPresent();

        Tag updatedTag = optionalTag.get();
        updatedTag.setName("Updated Tag");

        updatedTag = tagRepository.save(updatedTag);

        assertThat(updatedTag).isNotNull();
        assertThat(updatedTag.getId()).isEqualTo(4L);
        assertThat(updatedTag.getName()).isEqualTo("Updated Tag");
    }

    @Test
    @Rollback
    @DisplayName("tag dao: delete")
    void delete_deletesTag_whenIdIsValid() {
        Optional<Tag> optionalTag = tagRepository.findById(1L);

        assertThat(optionalTag).isPresent();

        Tag deletedTag = optionalTag.get();

        tagRepository.delete(deletedTag);

        assertThat(tagRepository.findAll()).isNotEmpty().hasSize(3);
    }

    @Test
    @DisplayName("tag dao: find all with filter and pagination")
    @Sql({"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-post-table.sql", "/db/insert-test-data-into-post_tag-table.sql"})
    void findAll_withFilterAndPagination_returnsFilteredAndPagedTags() {
        TagFilter filter = new TagFilter();
        filter.setPostId(1L);

        Page<Tag> tagsPage = tagRepository.findAll(TagSpecification.filterBy(filter), pageable);

        assertThat(tagsPage.getContent()).isNotEmpty().hasSize(1);
    }
}
