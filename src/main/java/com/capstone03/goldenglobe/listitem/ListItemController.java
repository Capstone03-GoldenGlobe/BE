package com.capstone03.goldenglobe.listitem;

import com.capstone03.goldenglobe.listgroup.ListGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ListItemController {

    private final ListItemService listItemService;

    @PostMapping("/checklists/{list_id}/{group_id}/items")
    public ResponseEntity<Map<String, Object>> postIem(@PathVariable Long list_id, @PathVariable Long group_id, @RequestParam String item_name) {

        ListItem listItem = listItemService.makeItem(list_id, group_id, item_name);

        // 응답 준비
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "항목 추가 완료");

        return ResponseEntity.ok(response);
    }
}
