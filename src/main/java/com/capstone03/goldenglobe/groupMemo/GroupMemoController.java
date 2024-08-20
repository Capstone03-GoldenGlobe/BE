package com.capstone03.goldenglobe.groupMemo;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Map<String, Object>> postMemo(@PathVariable Long group_id, @RequestParam String memo) {
        GroupMemo groupMemo = groupMemoService.makeMemo(group_id,memo);

        // 응답 준비
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "메모 입력완료");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/checklists/{group_id}/memos")
    public ResponseEntity<Map<String, Object>> editMemo(@PathVariable Long group_id, @RequestParam String memo){
        groupMemoService.editMemo(group_id, memo);
        // 응답 준비
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "항목 변경 완료");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/checklists/memos")
    public ResponseEntity<Map<String, Object>> deleteItem(@RequestParam Long memo_id) {
        groupMemoRepository.deleteById(memo_id);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "메모 삭제 완료");

        return ResponseEntity.ok(response);
    }
}
