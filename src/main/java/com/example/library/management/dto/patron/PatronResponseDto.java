package com.example.library.management.dto.patron;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatronResponseDto {
    private Long id;
    public  String name;
    private String email;
    private String phone;
}
