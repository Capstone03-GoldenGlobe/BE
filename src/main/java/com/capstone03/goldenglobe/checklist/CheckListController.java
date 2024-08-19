package com.capstone03.goldenglobe.checklist;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CheckListController {

    private final CheckListRepository checkListRepository;
    private final CheckListService checkListService;

    @PostMapping("/checklists")
    public ResponseEntity<Map<String, Object>> postChecklist(@RequestParam String destId) {
        String listId = checkListService.makeCheckList(destId);
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "체크리스트 추가 성공");
        response.put("list_id", listId);
        return ResponseEntity.ok(response);
    }
}
