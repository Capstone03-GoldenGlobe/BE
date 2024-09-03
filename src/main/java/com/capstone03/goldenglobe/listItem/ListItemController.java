package com.capstone03.goldenglobe.listItem;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
    public ResponseEntity<Map<String, Object>> editItemChecked(@PathVariable Long item_id, Authentication auth){
        ListItem updatedItem = listItemService.editItemChecked(item_id, auth);
        // 응답 준비
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "변경 완료");
        response.put("ischecked", updatedItem.isChecked());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/checklists/items/{item_id}/groups")
    public ResponseEntity<Map<String, Object>> editItemGroup(@PathVariable Long item_id, @RequestParam Long new_group_id, Authentication auth){
        ListItem updatedItem = listItemService.editItemGroup(item_id, new_group_id,auth);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "그룹 변경 완료");

        Map<String, Object> updated = new HashMap<>();
        updated.put("item_id",updatedItem.getItemId());
        updated.put("user_id", updatedItem.getUser() != null ? updatedItem.getUser().getUserId() : null);
        updated.put("item_name",updatedItem.getItem());
        updated.put("ischecked",updatedItem.isChecked());
        updated.put("group_id",updatedItem.getGroup().getGroupId());
        response.put("updatedItem",updated);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/checklists/items")
    public ResponseEntity<Map<String, Object>> deleteItem(@RequestParam Long item_id,Authentication auth) {
        listItemService.deleteItem(item_id,auth);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "항목 삭제 완료");

        return ResponseEntity.ok(response);
    }

}
