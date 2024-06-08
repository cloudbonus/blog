package com.github.blog.controller.util.marker;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

/**
 * @author Raman Haurylau
 */
public interface UserValidationGroup {

    @GroupSequence({BaseMarker.First.class, Default.class, BaseMarker.Second.class, BaseMarker.Third.class})
    interface onCreate {
    }

    @GroupSequence({BaseMarker.First.class, Default.class,})
    interface onAuthenticate {
    }

    @GroupSequence({Default.class, BaseMarker.Second.class, BaseMarker.Third.class})
    interface onUpdate {
    }
}
