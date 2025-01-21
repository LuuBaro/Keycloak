package org.example.asm_1.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private LocalDate dob;
}
