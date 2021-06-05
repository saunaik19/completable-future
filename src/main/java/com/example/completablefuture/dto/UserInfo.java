package com.example.completablefuture.dto;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
@Data
public class UserInfo {
    private String id;
    private String name;
    private String username;
    private String email;
    private List<Post> posts;
}
