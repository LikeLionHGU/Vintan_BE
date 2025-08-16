package com.vintan.dto.aiReport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CompanyDto {
    private String name;
    private String address;
    private String description;
}
