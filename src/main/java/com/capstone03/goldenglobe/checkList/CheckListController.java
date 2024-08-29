package com.capstone03.goldenglobe.checkList;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CheckListController {
    private final CheckListService checkListService;

    @PostMapping("/checklists")
    public ResponseEntity<Map<String, Object>> postChecklist(@RequestParam Long destId,Authentication auth) {
        String listId = checkListService.makeCheckList(destId,auth);
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "체크리스트 추가 성공");
        response.put("list_id", listId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/checklists/{dest_id}")
    public ResponseEntity<Map<String, Object>> getCheckList(@PathVariable Long dest_id,Authentication auth) {
        System.out.println("controller 실행");
        Map<String, Object> response = checkListService.getCheckListDetails(dest_id,auth);
        return ResponseEntity.ok(response);
    }
}
