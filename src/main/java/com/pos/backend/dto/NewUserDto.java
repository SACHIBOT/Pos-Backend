package com.pos.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewUserDto {

    private String username;
    private String password;
    private Long roleId;
}
