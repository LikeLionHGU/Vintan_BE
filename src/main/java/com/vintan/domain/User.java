package com.vintan.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @Column(name = "user_id")
    private String id;

    private String name;
    private String password;
    private String email;
    private int businessNumber;

    @Column(columnDefinition = "int default 0")
    private int point;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private BlindCommunityPost blindCommunityPost;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.mm.dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(updatable = false)
    private LocalDateTime regDate;
}
