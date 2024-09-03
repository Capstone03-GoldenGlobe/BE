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
    public ResponseEntity<Map<String, Object>> postItem(@PathVariable("list_id") Long listId, @PathVariable("group_id") Long groupId, @RequestParam("item_name") String itemName, Authentication auth) {

        ListItem listItem = listItemService.makeItem(listId, groupId, itemName, auth);

        // 응답 준비
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "항목 추가 완료");

        ListItemDTO toDto = ListItemDTO.fromEntity(listItem);
        response.put("item", toDto);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/checklists/items/{item_id}/name")
    public ResponseEntity<Map<String, Object>> editItemName(@PathVariable("item_id") Long itemId, @RequestParam("item_name") String itemName, Authentication auth){
        ListItem updatedItem = listItemService.editItemName(itemId, itemName,auth);
        // 응답 준비
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "항목 변경 완료");

        ListItemDTO updated = ListItemDTO.fromEntity(updatedItem);
        response.put("updatedItem", updated);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/checklists/items/{item_id}/checked")
    public ResponseEntity<Map<String, Object>> editItemChecked(@PathVariable("item_id") Long itemId, Authentication auth){
        ListItem updatedItem = listItemService.editItemChecked(itemId, auth);
        // 응답 준비
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "체크/체크해제 변경 완료");

        ListItemDTO updated = ListItemDTO.fromEntity(updatedItem);
        response.put("updatedItem", updated);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/checklists/items/{item_id}/groups")
    public ResponseEntity<Map<String, Object>> editItemGroup(@PathVariable("item_id") Long itemId, @RequestParam("new_group_id") Long newGroupId, Authentication auth){
        ListItem updatedItem = listItemService.editItemGroup(itemId, newGroupId,auth);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "그룹 변경 완료");

        ListItemDTO updated = ListItemDTO.fromEntity(updatedItem);
        response.put("updatedItem", updated);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/checklists/items")
    public ResponseEntity<Map<String, Object>> deleteItem(@RequestParam("item_id") Long itemId,Authentication auth) {
        listItemService.deleteItem(itemId,auth);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "항목 삭제 완료");

        return ResponseEntity.ok(response);
    }

}
