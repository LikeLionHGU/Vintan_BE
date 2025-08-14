package com.vintan.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "댓글")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QnaComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId", nullable = false) // FK → QnaPost.postId
    private QnaPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", referencedColumnName = "userid", nullable = false) // FK → User.userid
    private User user;


    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "text_time")
    private LocalDate textTime;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;  // ★ 추가: 생성 시각// 유지 (원본 ERD 호환)
}