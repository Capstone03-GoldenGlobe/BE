package com.capstone03.goldenglobe.listItem;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ListItemController {

    private final ListItemService listItemService;
    private final ListItemRepository listItemRepository;

    @PostMapping("/checklists/{list_id}/{group_id}/items")
    public ResponseEntity<Map<String, Object>> postItem(@PathVariable Long list_id, @PathVariable Long group_id, @RequestParam String item_name, Authentication auth) {

        ListItem listItem = listItemService.makeItem(list_id, group_id, item_name, auth);

        // 응답 준비
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "항목 추가 완료");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/checklists/items/{item_id}/name")
    public ResponseEntity<Map<String, Object>> editItemName(@PathVariable Long item_id, @RequestParam String item_name, Authentication auth){
        listItemService.editItemName(item_id, item_name,auth);
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

    @DeleteMapping("/checklists/items")
    public ResponseEntity<Map<String, Object>> deleteItem(@RequestParam Long item_id) {
        listItemRepository.deleteById(item_id);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "항목 삭제 완료");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/checklists/items/{item_id}/groups")
    public ResponseEntity<Map<String, Object>> editItemGroup(@PathVariable Long item_id, @RequestParam Long new_group_id){
        listItemService.editItemGroup(item_id, new_group_id);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "그룹 변경 완료");

        return ResponseEntity.ok(response);
    }

}
