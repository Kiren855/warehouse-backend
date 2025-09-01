package com.sunny.scm.identity.entity;

import com.sunny.scm.common.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "groups",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"company_id", "group_name"})
})
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Group extends BaseEntity {
    @Column(name = "company_id")
    Long companyId;

    @Column(name = "group_name")
    String groupName;

    @Column(name = "create_by")
    String createdBy;
}
