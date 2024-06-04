package com.github.blog.controller.util.marker;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

/**
 * @author Raman Haurylau
 */
public interface BaseMarker {
    interface First {
    }

    interface Second {
    }

    interface Third {
    }

    interface Create {
    }

    interface Update {
    }

    @GroupSequence({Create.class, Default.class})
    interface onCreate {
    }

    @GroupSequence({Update.class, Default.class})
    interface onUpdate {
    }
}
