package com.capstone03.goldenglobe.groupMemo;


import com.capstone03.goldenglobe.listGroup.ListGroupDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class GroupMemoController {

    private final GroupMemoService groupMemoService;
    private final GroupMemoRepository groupMemoRepository;
    @PostMapping("/checklists/{group_id}/memos")
    public ResponseEntity<Map<String, Object>> postMemo(@PathVariable("group_id") Long groupId, @RequestParam String memo, Authentication auth) {
        GroupMemo groupMemo = groupMemoService.makeMemo(groupId,memo, auth);

        // 응답 준비
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "메모 입력완료");

        GroupMemoDTO toDto = GroupMemoDTO.fromEntity(groupMemo);
        response.put("memo",toDto);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/checklists/{group_id}/memos")
    public ResponseEntity<Map<String, Object>> editMemo(@PathVariable("group_id") Long groupId, @RequestParam String memo, Authentication auth){
        GroupMemo updatedMemo = groupMemoService.editMemo(groupId, memo, auth);
        // 응답 준비
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "메모 변경 완료");

        GroupMemoDTO updated = GroupMemoDTO.fromEntity(updatedMemo);
        response.put("memo",updated);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/checklists/memos")
    public ResponseEntity<Map<String, Object>> deleteItem(@RequestParam("memo_id") Long memoId, Authentication auth) {

        groupMemoService.deleteMemo(memoId, auth);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "메모 삭제 완료");

        return ResponseEntity.ok(response);
    }
}
