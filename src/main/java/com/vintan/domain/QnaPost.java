package com.vintan.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a Q&A post.
 * Stores the post title, content, author, creation timestamp, and associated comments.
 */
@Entity
@Table(name = "qna_community_posts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QnaPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Primary key, auto-generated
    @Column(name = "post_id")
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user; // FK → User.user_id (author of the post)

    @Column(name = "title", nullable = false, length = 200)
    private String title; // Title of the Q&A post

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content; // Main content of the post

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // Timestamp when the post was created

    /**
     * List of comments associated with this post.
     * Cascade all operations and remove orphans automatically.
     */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<QnaComment> comments = new ArrayList<>();
}
