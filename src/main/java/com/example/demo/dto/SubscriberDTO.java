package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
public class SubscriberDTO {
    private String email;
    private String firstName;
    private String lastName;
    private Long userId;
}
