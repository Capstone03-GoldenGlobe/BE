package com.capstone03.goldenglobe.sharedList;

import com.capstone03.goldenglobe.listGroup.ListGroup;
import com.capstone03.goldenglobe.listItem.ListItemDTO;
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
    public ResponseEntity<Map<String, Object>> postGroup(@PathVariable("list_id") Long listId, @RequestParam("user_id") Long userId, Authentication auth) {
        sharedListService.addUser(listId, userId, auth);

        // 응답 준비
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "체크리스트 공유 성공");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/checklists/share/{list_id}/color")
    public ResponseEntity<Map<String, Object>> changeColor(@PathVariable("list_id") Long listId, @RequestParam("user_color") String userColor, Authentication auth) {
        SharedList updatedShared = sharedListService.changeColor(listId, userColor, auth);

        // 응답 준비
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "공유 색상 변경");

        SharedListDTO updated = SharedListDTO.fromEntity(updatedShared);
        response.put("updatedShared", updated);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/checklists/share/{list_id}") // 공유 설정 해제
    public ResponseEntity<Map<String, Object>> deleteShare(@PathVariable("list_id") Long listId, Authentication auth) {
        // 삭제 처리 service 함수
        sharedListService.deleteShare(listId,auth);

        // 응답 준비
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "공유 해제");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/checklists/share/{list_id}/{user_id}") // 공유 설정 해제
    public ResponseEntity<Map<String, Object>> deleteShare(@PathVariable("list_id") Long listId, @PathVariable("user_id") Long userId, Authentication auth) {
        // 삭제 처리 service 함수
        sharedListService.deleteShareOwner(listId, userId, auth);

        // 응답 준비
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "공유 해제");

        return ResponseEntity.ok(response);
    }
}
