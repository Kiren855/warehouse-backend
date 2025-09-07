package com.sunny.scm.identity.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum RealmRoles {
    ROOT("e2076e2b-f355-4ac1-85ae-47bea4fcd987", "ROOT"),
    SUB("b6d17c1a-947d-4f1c-9be2-ee3ade63ed1f", "SUB"),
    ADMIN("189019e1-118c-40a3-b089-78c60c2b7984", "ADMIN")
;
    String id;
    String name;

    RealmRoles(String id, String name) {
        this.id = id;
        this.name = name;
    }


}
