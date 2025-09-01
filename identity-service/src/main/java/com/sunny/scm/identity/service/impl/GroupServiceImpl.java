package com.sunny.scm.identity.service.impl;

import com.sunny.scm.identity.repository.GroupRepository;
import com.sunny.scm.identity.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    @Override
    public void createGroup() {

    }

    @Override
    public void deleteGroup() {

    }

    @Override
    public void updateGroup() {

    }
}
