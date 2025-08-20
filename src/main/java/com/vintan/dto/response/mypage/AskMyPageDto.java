
package com.vintan.dto.response.mypage;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AskMyPageDto {


    private String id;          // 사용자 id (명세 그대로)


    private String title;       // 질문 제목


    private Integer countComment;// -댓글 개수


    private String date;        // 작성 날짜(yyyy.mm.dd)


}