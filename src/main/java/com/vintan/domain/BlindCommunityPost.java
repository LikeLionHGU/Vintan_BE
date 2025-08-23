package com.vintan.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vintan.domain.embedded.CategoryRate;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entity representing an anonymous community post.
 * Stores user feedback, ratings, and metadata for a specific region.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class) // Enables automatic auditing of creation and update timestamps
public class BlindCommunityPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blindCommunityPost_id")
    private Long id; // Primary key

    private String title;    // Post title
    private String positive; // Positive feedback content
    private String negative; // Negative feedback content
    private String address;  // Related location/address
    private Long regionNo;   // Region ID to categorize posts

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // Reference to the user who created the post (lazy loading)

    @Embedded
    private CategoryRate categoryRate; // Embedded category ratings

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(updatable = false)
    private LocalDateTime regDate; // Creation timestamp

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updateDate; // Last updated timestamp
}
