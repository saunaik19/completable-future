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
public class Post {
    private String userId;
    private String id;
    private String title;
    private String body;
    private List<Comment> comments;
}
