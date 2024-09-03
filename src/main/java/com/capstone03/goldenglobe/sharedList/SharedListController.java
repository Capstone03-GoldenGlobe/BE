package com.capstone03.goldenglobe.sharedList;

import com.capstone03.goldenglobe.listGroup.ListGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SharedListController {

    private final SharedListService sharedListService;

    @PostMapping("/checklists/share/{list_id}")
    public ResponseEntity<Map<String, Object>> postGroup(@PathVariable Long list_id, @RequestParam Long user_id, Authentication auth) {
        sharedListService.addUser(list_id, user_id, auth);

        // 응답 준비
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "체크리스트 공유 성공");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/checklists/share/{list_id}/color")
    public ResponseEntity<Map<String, Object>> changeColor(@PathVariable Long list_id, @RequestParam String user_color, Authentication auth) {
        sharedListService.changeColor(list_id, user_color, auth);

        // 응답 준비
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "공유 색상 변경");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/checklists/share/{list_id}") // 공유 설정 해제
    public ResponseEntity<Map<String, Object>> deleteShare(@PathVariable Long list_id, Authentication auth) {
        // 삭제 처리 service 함수
        sharedListService.deleteShare(list_id,auth);

        // 응답 준비
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "공유 해제");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/checklists/share/{list_id}/{user_id}") // 공유 설정 해제
    public ResponseEntity<Map<String, Object>> deleteShare(@PathVariable Long list_id, @PathVariable Long user_id, Authentication auth) {
        // 삭제 처리 service 함수
        sharedListService.deleteShareOwner(list_id, user_id, auth);

        // 응답 준비
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "공유 해제");

        return ResponseEntity.ok(response);
    }
}
