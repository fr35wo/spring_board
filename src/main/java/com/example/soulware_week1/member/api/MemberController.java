package com.example.soulware_week1.member.api;

import com.example.soulware_week1.global.jwt.domain.CustomUserDetail;
import com.example.soulware_week1.global.template.RspTemplate;
import com.example.soulware_week1.member.application.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "member", description = "Member Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "로그인 성공(최초 로그인 구별)", description = "로그인 시에 불러올 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @GetMapping("/success")
    public RspTemplate<Map<String, Object>> isFirstLogin(@AuthenticationPrincipal CustomUserDetail member) {

        boolean isFirstLogin = memberService.checkAndUpdateFirstLogin(member.getMember());

        Map<String, Object> response = new HashMap<>();
        response.put("isFirstLogin", isFirstLogin);

        return new RspTemplate<>(HttpStatus.OK, "최초 로그인 상태입니다.", response);
    }
}

