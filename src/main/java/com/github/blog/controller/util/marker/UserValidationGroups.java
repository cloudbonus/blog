package com.github.blog.controller.util.marker;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

/**
 * @author Raman Haurylau
 */
public interface UserValidationGroups {
    @GroupSequence({Marker.First.class, Marker.Second.class, Marker.Third.class})
    interface UserCreateValidationGroupSequence {}

    @GroupSequence(Marker.First.class)
    interface UserAuthenticateValidationGroupSequence {}

    @GroupSequence({Marker.Second.class, Marker.Third.class})
    interface UserUpdateValidationGroupSequence {}

    @GroupSequence({Marker.First.class, Default.class})
    interface UserInfoCreateValidationGroupSequence {}

    @GroupSequence({Marker.Second.class, Default.class})
    interface UserInfoUpdateValidationGroupSequence {}
}
