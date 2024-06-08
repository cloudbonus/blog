package com.github.blog.controller.util.marker;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

/**
 * @author Raman Haurylau
 */
public interface UserInfoValidationGroup {

    @GroupSequence({BaseMarker.First.class, Default.class})
    interface onCreate {
    }
}
