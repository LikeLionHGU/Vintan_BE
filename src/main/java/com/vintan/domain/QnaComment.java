package com.vintan.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

/**
 * Entity representing a comment on a Q&A post.
 * Stores the comment text, the user who posted it, and the timestamp.
 */
@Entity
@Table(name = "qna_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QnaComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key for the comment

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private QnaPost post;   // FK to QnaPost (many comments can belong to one post)

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;      // FK to User (author of the comment)

    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;    // Comment content (TEXT for long content)

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime textTime; // Timestamp when comment was created
}
