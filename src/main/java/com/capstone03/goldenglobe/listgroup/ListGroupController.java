package com.capstone03.goldenglobe.listgroup;

import com.capstone03.goldenglobe.checklist.CheckList;
import com.capstone03.goldenglobe.checklist.CheckListRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Check;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ListGroupController {

    private final ListGroupRepository listGroupRepository;
    private final ListGroupService listGroupService;

    @PostMapping("/checklists/{list_id}")
    public ResponseEntity<Map<String, Object>> postGroup(@PathVariable Long list_id, @RequestParam String group_name) {
        ListGroup listGroup = listGroupService.makeGroup(list_id,group_name);

        // 응답 준비
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "그룹 추가 성공");

        Map<String, String> data = new HashMap<>();
        data.put("group_id", listGroup.getGroupId().toString());
        data.put("group_name", listGroup.getGroupName());
        response.put("data", data);

        return ResponseEntity.ok(response);
    }
}
