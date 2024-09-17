package com.capstone03.goldenglobe.checkList;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="CheckList",description = "체크리스트  API")
public class CheckListController {
    private final CheckListService checkListService;

    @PostMapping("/checklists")
    @Operation(summary="체크리스트 생성",description = "주어진 목적지Id에 대한 체크리스트 생성. 현재는 여행지 설정 시 자동으로 생성되므로 필요 없는 API")
    public ResponseEntity<Map<String, Object>> postChecklist(@RequestParam Long destId,Authentication auth) {
        String listId = checkListService.makeCheckList(destId,auth);
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "체크리스트 추가 성공");
        response.put("list_id", listId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/checklists/{dest_id}")
    @Operation(summary = "체크리스트 조회",description = "주어진 목적지Id에 대한 체크리스트 전체 조회(그룹, 메모, 항목)")
    public ResponseEntity<Map<String, Object>> getCheckList(@PathVariable("dest_id") Long dest_id,Authentication auth) {
        System.out.println("controller 실행");
        Map<String, Object> response = checkListService.getCheckListDetails(dest_id,auth);
        return ResponseEntity.ok(response);
    }
}
