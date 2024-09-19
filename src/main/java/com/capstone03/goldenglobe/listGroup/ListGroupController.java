package com.capstone03.goldenglobe.listGroup;

import com.capstone03.goldenglobe.ApiResponseSetting;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Tag(name="ListGroup",description = "체크리스트 내 그룹 API")
public class ListGroupController {

    private final ListGroupService listGroupService;
    private final ListGroupRepository listGroupRepository;

    @PostMapping("/checklists/{list_id}/groups")
    @Operation(summary="그룹 생성", description = "체크리스트Id에 속하는 그룹 생성")
    public ResponseEntity<ApiResponseSetting<ListGroupDTO>> postGroup(@PathVariable("list_id") Long listId, @RequestParam("group_name") String groupName, Authentication auth) {
        ListGroup listGroup = listGroupService.makeGroup(listId,groupName, auth);
        ListGroupDTO toDto = ListGroupDTO.fromEntity(listGroup);
        ApiResponseSetting<ListGroupDTO> response = new ApiResponseSetting<>(200,"그룹 생성 성공",toDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/checklists/groups/{group_id}")
    @Operation(summary = "그룹 이름 수정",description = "그룹Id로 그룹 이름 수정")
    public ResponseEntity<ApiResponseSetting<ListGroupDTO>> editGroupName(@PathVariable("group_id") Long groupId, @RequestParam("group_name") String groupName, Authentication auth){
        ListGroup updatedGroup = listGroupService.editGroupName(groupId, groupName, auth);
        ListGroupDTO toDto = ListGroupDTO.fromEntity(updatedGroup);
        ApiResponseSetting<ListGroupDTO> response = new ApiResponseSetting<>(200,"그룹 이름 변경 성공",toDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/checklists/groups")
    @Operation(summary = "그룹 삭제",description = "그룹Id로 그룹 삭제")
    public ResponseEntity<ApiResponseSetting<Void>> deleteItem(@RequestParam("group_id") Long groupId, Authentication auth) {
        listGroupService.deleteGroup(groupId, auth);
        ApiResponseSetting<Void> response = new ApiResponseSetting<>(200, "그룹 삭제 완료(메모, 항목 포함)", null);
        return ResponseEntity.ok(response);
    }
}
