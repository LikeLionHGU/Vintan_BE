package com.vintan.dto.response.user;

import com.vintan.domain.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUserDto implements Serializable {
    private String id;
    private String name;
    private String email;
    private int businessNumber;
    private int point;

    public SessionUserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.businessNumber = user.getBusinessNumber();
        this.point = user.getPoint();
    }
}
