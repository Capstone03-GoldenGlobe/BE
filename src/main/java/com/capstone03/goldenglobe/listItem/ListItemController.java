package com.capstone03.goldenglobe.listItem;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ListItemController {

    private final ListItemService listItemService;

    @PostMapping("/checklists/{list_id}/{group_id}/items")
    public ResponseEntity<Map<String, Object>> postItem(@PathVariable Long list_id, @PathVariable Long group_id, @RequestParam String item_name) {

        ListItem listItem = listItemService.makeItem(list_id, group_id, item_name);

        // 응답 준비
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "항목 추가 완료");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/checklists/items/{item_id}/name")
    public ResponseEntity<Map<String, Object>> editItemName(@PathVariable Long item_id, @RequestParam String item_name){
        listItemService.editItemName(item_id, item_name);
        // 응답 준비
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "항목 변경 완료");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/checklists/items/{item_id}/checked")
    public ResponseEntity<Map<String, Object>> editItemChecked(@PathVariable Long item_id){
        ListItem updatedItem = listItemService.editItemChecked(item_id);
        // 응답 준비
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "변경 완료");
        response.put("ischecked", updatedItem.isChecked());

        return ResponseEntity.ok(response);
    }
}
