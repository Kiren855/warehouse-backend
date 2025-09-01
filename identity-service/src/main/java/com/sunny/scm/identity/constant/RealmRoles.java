package com.sunny.scm.identity.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum RealmRoles {
    ROOT("14cfee5d-04cd-48be-b55d-22b7312d0e32", "ROOT"),
    SUB("5b1faf33-f802-4d05-8609-b475ad7675c7", "SUB")
;
    String id;
    String name;

    RealmRoles(String id, String name) {
        this.id = id;
        this.name = name;
    }


}
