package com.vintan.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vintan.embedded.CategoryRate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BlindCommunityPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "blindCommunityPost_id")
    private Long id;

    private String title;
    private String positive;
    private String negative;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Embedded
    private CategoryRate categoryRate;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.mm.dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.mm.dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updateDate;
}
