package com.capstone03.goldenglobe.sharedList;

import com.capstone03.goldenglobe.ApiResponseSetting;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Tag(name="SharedList",description = "체크리스트 공유 관련 API")
public class SharedListController {

    private final SharedListService sharedListService;

    @PostMapping("/checklists/share/{list_id}")
    @Operation(summary = "체크리스트 공유",description = "유저 전화번호를 입력받아 해당 유저와 체크리스트를 공유")
    public ResponseEntity<ApiResponseSetting<SharedListDTO>> postShared(@PathVariable("list_id") Long listId, @RequestParam("cellPhone") String cellPhone, Authentication auth) {
        SharedList sharedList = sharedListService.addUser(listId, cellPhone, auth);
        SharedListDTO toDto = SharedListDTO.fromEntity(sharedList);
        ApiResponseSetting<SharedListDTO> response = new ApiResponseSetting<>(200,"체크리스트 공유 성공",toDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/checklists/share/{list_id}")
    @Operation(summary = "공유 사용자 조회",description = "현재 체크리스트를 공유받은 사용자를 조회합니다.")
    public ResponseEntity<ApiResponseSetting<List<SharedListDTO>>> getShared(@PathVariable("list_id") Long listId, Authentication auth){
        List<SharedListDTO> sharedListDTOs = sharedListService.getSharedUsers(listId, auth);
        ApiResponseSetting<List<SharedListDTO>> response = new ApiResponseSetting<>(200,"공유 체크리스트 조회",sharedListDTOs);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/checklists/share/{list_id}/color")
    @Operation(summary = "공유 색상 변경",description = "체크리스트에서 표시되는 색상을 변경합니다. (6자리 헥스코드로 입력. 예:#ff0099")
    public ResponseEntity<ApiResponseSetting<SharedListDTO>> changeColor(@PathVariable("list_id") Long listId, @RequestParam("user_color") String userColor, Authentication auth) {
        SharedList updatedShared = sharedListService.changeColor(listId, userColor, auth);
        SharedListDTO toDto = SharedListDTO.fromEntity(updatedShared);
        ApiResponseSetting<SharedListDTO> response = new ApiResponseSetting<>(200,"공유 색상 변경",toDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/checklists/share/{list_id}") // 공유 설정 해제
    @Operation(summary = "공유 해제",description = "공유 받은 체크리스트를 해제합니다.")
    public ResponseEntity<ApiResponseSetting<Void>> deleteShare(@PathVariable("list_id") Long listId, Authentication auth) {
        // 삭제 처리 service 함수
        sharedListService.deleteShare(listId,auth);
        ApiResponseSetting<Void> response = new ApiResponseSetting<>(200,"공유 해제 성공");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/checklists/share/{list_id}/{user_id}") // 공유 설정 해제
    @Operation(summary = "특정 사용자의 공유 해제",description = "공유한 유저가 더 이상 체크리스트에 접근할 수 없게 공유를 해제합니다.")
    public ResponseEntity<ApiResponseSetting<Void>> deleteShare(@PathVariable("list_id") Long listId, @PathVariable("user_id") Long userId, Authentication auth) {
        // 삭제 처리 service 함수
        sharedListService.deleteShareOwner(listId, userId, auth);

        ApiResponseSetting<Void> response = new ApiResponseSetting<>(200,"공유 해제 성공");
        return ResponseEntity.ok(response);
    }
}
