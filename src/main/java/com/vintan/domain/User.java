package com.vintan.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Report> reports = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<QnaComment> qnaComments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<QnaPost> qnaPosts = new ArrayList<>();

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.mm.dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(updatable = false)
    private LocalDateTime regDate;
}