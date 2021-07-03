package com.example.completablefuture.dto;


import lombok.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    private List<Todo> todos;
    
    public UserInfo(String userId,UserInfo userObj,List<Post> posts){
        this.setId(userObj.id);
        this.setName(userObj.getName());
        this.setUsername(userObj.getUsername());
        this.setEmail(userObj.getEmail());
        this.setPosts(posts);
    }
    public UserInfo(String userId,UserInfo userObj,List<Post> posts,List<Todo> toDos){
        this.setId(userObj.id);
        this.setName(userObj.getName());
        this.setUsername(userObj.getUsername());
        this.setEmail(userObj.getEmail());
        this.setPosts(posts);
        this.setTodos(toDos);
    }

}
