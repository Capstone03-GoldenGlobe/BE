package com.capstone03.goldenglobe.listGroup;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ListGroupController {

    private final ListGroupService listGroupService;
    private final ListGroupRepository listGroupRepository;

    @PostMapping("/checklists/{list_id}/groups")
    public ResponseEntity<Map<String, Object>> postGroup(@PathVariable Long list_id, @RequestParam String group_name, Authentication auth) {
        ListGroup listGroup = listGroupService.makeGroup(list_id,group_name, auth);

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

    @PutMapping("/checklists/groups/{group_id}")
    public ResponseEntity<Map<String, Object>> editGroupName(@PathVariable Long group_id, @RequestParam String group_name, Authentication auth){
        listGroupService.editGroupName(group_id, group_name, auth);
        // 응답 준비
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "그룹 이름 변경 성공");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/checklists/groups")
    public ResponseEntity<Map<String, Object>> deleteItem(@RequestParam Long group_id,Authentication auth) {
        listGroupService.deleteGroup(group_id, auth);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "그룹 삭제 완료 (그룹 메모, 아이템 포함)");

        return ResponseEntity.ok(response);
    }
}
