package com.vintan.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterRequestDto {

    @NotBlank(message = "id is compulsory")
    private String id;

    @NotBlank(message = "name is compulsory")
    private String name;

    @NotBlank(message = "password is compulsory")
    // @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.") 이렇게 설정가능
    private String password;

    @NotBlank(message = "email is compulsory")
    @Email(message = "wrong email format")
    private String email;

    private int businessNumber;


}
