package com.vintan.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "User") // DB 테이블명
public class User {

    @Id // @GeneratedValue(strategy = GenerationType.IDENTITY)안쓰는 이유는 사용자가 id를 설정하기 때문.
    @Column(name = "userid", length = 100)
    private String userid; // PK, 문자열

    @Column(name = "password", nullable = false, length = 255)
    private String password; // 비밀번호 (해시 저장 권장)

    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email; // 이메일 (유니크)

    @Column(name = "name", nullable = false, length = 100)
    private String name; // 이름

    @Column(name = "bussinessNumber", nullable = false)
    private Integer bussinessNumber; // 사업자 번호 (10자리, 정수형)

    @Column(name = "point")
    private Integer point; // 포인트 (기본 0)
}