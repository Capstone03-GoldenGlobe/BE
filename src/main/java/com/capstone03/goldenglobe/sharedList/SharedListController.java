package com.capstone03.goldenglobe.sharedList;

import com.capstone03.goldenglobe.listGroup.ListGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
