package com.example.completablefuture.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
@Data
public class Todo {
    private String userId                      ;
    private String id                          ;
    private String title                       ;
    private String completed                   ;
}
