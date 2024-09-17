package com.capstone03.goldenglobe.listItem;

import com.capstone03.goldenglobe.ApiResponse;
import com.capstone03.goldenglobe.groupMemo.GroupMemoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="ListItem",description = "체크리스트 항목(아이템) API")
public class ListItemController {

    private final ListItemService listItemService;
    private final ListItemRepository listItemRepository;



    @PostMapping("/checklists/{list_id}/{group_id}/items")
    @Operation(summary = "항목 추가",description = "그룹Id에 속하는 아이템 추가")
    public ResponseEntity<ApiResponse<ListItemDTO>> postItem(@PathVariable("list_id") Long listId, @PathVariable("group_id") Long groupId, @RequestParam("item_name") String itemName, Authentication auth) {
        ListItem listItem = listItemService.makeItem(listId, groupId, itemName, auth);
        ListItemDTO toDto = ListItemDTO.fromEntity(listItem);
        ApiResponse<ListItemDTO> response = new ApiResponse<>(200,"항목 추가 완료",toDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/checklists/items/{item_id}/name")
    @Operation(summary = "항목 수정",description = "항목Id로 아이템 수정")
    public ResponseEntity<ApiResponse<ListItemDTO>> editItemName(@PathVariable("item_id") Long itemId, @RequestParam("item_name") String itemName, Authentication auth){
        ListItem updatedItem = listItemService.editItemName(itemId, itemName,auth);
        ListItemDTO toDto = ListItemDTO.fromEntity(updatedItem);
        ApiResponse<ListItemDTO> response = new ApiResponse<>(200,"항목 변경 완료",toDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/checklists/items/{item_id}/checked")
    @Operation(summary = "항목 체크/체크 해제",description = "항목Id로 항목을 체크/체크해제. 항목 체크 시 체크한 유저Id 기재")
    public ResponseEntity<ApiResponse<ListItemDTO>> editItemChecked(@PathVariable("item_id") Long itemId, Authentication auth){
        ListItem updatedItem = listItemService.editItemChecked(itemId, auth);
        ListItemDTO toDto = ListItemDTO.fromEntity(updatedItem);
        ApiResponse<ListItemDTO> response = new ApiResponse<>(200,"체크/체크해제 변경 완료", toDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/checklists/items/{item_id}/groups")
    @Operation(summary = "항목 그룹 변경",description = "항목Id와 새 그룹Id로 항목의 그룹을 변경")
    public ResponseEntity<ApiResponse<ListItemDTO>> editItemGroup(@PathVariable("item_id") Long itemId, @RequestParam("new_group_id") Long newGroupId, Authentication auth){
        ListItem updatedItem = listItemService.editItemGroup(itemId, newGroupId,auth);
        ListItemDTO toDto = ListItemDTO.fromEntity(updatedItem);
        ApiResponse<ListItemDTO> response = new ApiResponse<>(200,"그룹 변경 완료", toDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/checklists/items")
    @Operation(summary = "항목 삭제",description = "항목Id로 항목 삭제")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@RequestParam("item_id") Long itemId,Authentication auth) {
        listItemService.deleteItem(itemId,auth);
        ApiResponse<Void> response = new ApiResponse<>(200, "항목 삭제 완료", null);
        return ResponseEntity.ok(response);
    }

}
