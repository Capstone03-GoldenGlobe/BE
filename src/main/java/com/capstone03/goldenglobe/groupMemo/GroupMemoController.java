package com.capstone03.goldenglobe.groupMemo;


import com.capstone03.goldenglobe.ApiResponseSetting;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name="GroupMemo",description = "그룹 메모  API")
public class GroupMemoController {

    private final GroupMemoService groupMemoService;
    private final GroupMemoRepository groupMemoRepository;
    @PostMapping("/checklists/{group_id}/memos")
    @Operation(summary = "메모 생성",description = "그룹Id를 이용해 메모 생성")
    public ResponseEntity<ApiResponseSetting<GroupMemoDTO>> postMemo(@PathVariable("group_id") Long groupId, @RequestParam String memo, Authentication auth) {
        GroupMemo groupMemo = groupMemoService.makeMemo(groupId,memo, auth);
        GroupMemoDTO toDto = GroupMemoDTO.fromEntity(groupMemo);
        ApiResponseSetting<GroupMemoDTO> response = new ApiResponseSetting<>(200,"메모 입력 완료", toDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/checklists/{group_id}/memos")
    @Operation(summary = "메모 수정",description = "그룹Id로 메모 내용 수정")
    public ResponseEntity<ApiResponseSetting<GroupMemoDTO>> editMemo(@PathVariable("group_id") Long groupId, @RequestParam String memo, Authentication auth){
        GroupMemo updatedMemo = groupMemoService.editMemo(groupId, memo, auth);
        GroupMemoDTO toDto = GroupMemoDTO.fromEntity(updatedMemo);
        ApiResponseSetting<GroupMemoDTO> response = new ApiResponseSetting<>(200,"메모 수정 완료", toDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/checklists/memos")
    @Operation(summary = "메모 삭제",description = "메모Id로 메모 삭제")
    public ResponseEntity<ApiResponseSetting<Void>> deleteItem(@RequestParam("memo_id") Long memoId, Authentication auth) {
        groupMemoService.deleteMemo(memoId, auth);
        ApiResponseSetting<Void> response = new ApiResponseSetting<>(200, "메모 삭제 완료", null);
        return ResponseEntity.ok(response);
    }
}