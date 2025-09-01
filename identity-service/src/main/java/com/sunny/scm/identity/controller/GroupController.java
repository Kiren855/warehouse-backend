package com.sunny.scm.identity.controller;

import com.sunny.scm.identity.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/identity/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {
    private GroupService groupService;

}
