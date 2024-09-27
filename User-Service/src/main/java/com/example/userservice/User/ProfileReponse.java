package com.example.userservice.User;

import com.example.userservice.Entity.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileReponse {
    private String message;
    private User data;
}
