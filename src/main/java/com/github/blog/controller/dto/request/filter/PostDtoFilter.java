package com.github.blog.controller.dto.request.filter;

import com.github.blog.service.statemachine.state.OrderState;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class PostDtoFilter {
    private String username;
    private Long tagId;
    private String state;

    public PostDtoFilter() {
        this.state = OrderState.COMPLETED.name();
    }
}
