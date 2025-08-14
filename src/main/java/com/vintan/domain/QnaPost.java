package com.vintan.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "QNACommunityPosts")

public class QnaPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //pk 자동 생성
    @Column(name = "postId")
    private Integer postId;

    @Column(name = "userid", nullable = false, length = 100) //DB 컬럼을 userid로 지정. NULL값을 허용x
    private String userId;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;   // ← 목록/상세에서 보여줄 작성일


}
