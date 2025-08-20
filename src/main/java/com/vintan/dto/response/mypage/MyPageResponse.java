package com.vintan.dto.response.mypage;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class MyPageResponse {


    private String email;         // 사용자 e-mail


    private String id;            // 사용자 id


    private BlindMyPageDto Blind;       // 내가 블라인드에 쓴 글 1건 (nullable 가능)


    private List<AskMyPageDto> Ask;     // 내가 QnA 게시판에서 입력한 글 최대 3개


    private String name;          // 사용자 이름


    private Integer point;        // 보유 포인트


    private Integer businessNumber;// 사업자 번호


    private AiReportMyPageDto aiReport; // AI가 생성한 보고서 요약 (nullable 가능)


}