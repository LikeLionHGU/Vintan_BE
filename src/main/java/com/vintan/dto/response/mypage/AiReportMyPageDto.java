package com.vintan.dto.response.mypage;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiReportMyPageDto {


    private String id;          // 사용자 id


    private String address;     // 입지(최근 보고서의 address)


    private Integer reportCount;// 보고서 개수


    private String date;        // 작성 날짜(yyyy.mm.dd)


}