package com.capstone03.goldenglobe.listGroup;

import com.capstone03.goldenglobe.listItem.ListItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ListGroupController {

    private final ListGroupService listGroupService;
    private final ListGroupRepository listGroupRepository;

    @PostMapping("/checklists/{list_id}/groups")
    public ResponseEntity<Map<String, Object>> postGroup(@PathVariable("list_id") Long listId, @RequestParam("group_name") String groupName, Authentication auth) {
        ListGroup listGroup = listGroupService.makeGroup(listId,groupName, auth);

        // 응답 준비
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "그룹 추가 성공");

        ListGroupDTO toDto = ListGroupDTO.fromEntity(listGroup);
        response.put("group",toDto);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/checklists/groups/{group_id}")
    public ResponseEntity<Map<String, Object>> editGroupName(@PathVariable("group_id") Long groupId, @RequestParam("group_name") String groupName, Authentication auth){
        ListGroup updatedGroup = listGroupService.editGroupName(groupId, groupName, auth);
        // 응답 준비
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "그룹 이름 변경 성공");

        ListGroupDTO updated = ListGroupDTO.fromEntity(updatedGroup);
        response.put("updatedGroup",updated);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/checklists/groups")
    public ResponseEntity<Map<String, Object>> deleteItem(@RequestParam("group_id") Long groupId,Authentication auth) {
        listGroupService.deleteGroup(groupId, auth);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "그룹 삭제 완료 (그룹 메모, 아이템 포함)");

        return ResponseEntity.ok(response);
    }
}
