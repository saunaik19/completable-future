package com.example.completablefuture.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
@Data
public class Comment {
    private String postId    ;
    private String id        ;
    private String name      ;
    private String email     ;
    private String body      ;
}
