package com.vintan.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a user of the platform.
 * Contains personal information, business data, and relationships to posts and reports.
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @Column(name = "user_id")
    private String id; // Primary key, user ID

    private String name; // User's display name
    private String password; // Encrypted password
    private String email; // User email
    private int businessNumber; // Business registration number; 0 if not a business user

    @Column(columnDefinition = "int default 0")
    private int point; // User points for rewards or gamification

    // One-to-one relationship with a BlindCommunityPost
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private BlindCommunityPost blindCommunityPost;

    // One-to-many relationship with reports authored by the user
    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Report> reports = new ArrayList<>();

    // One-to-many relationship with Q&A comments authored by the user
    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<QnaComment> qnaComments = new ArrayList<>();

    // One-to-many relationship with Q&A posts authored by the user
    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<QnaPost> qnaPosts = new ArrayList<>();

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(updatable = false)
    private LocalDateTime regDate; // Timestamp of user registration
}
