package com.example.completablefuture.service;

import com.example.completablefuture.dto.Comment;
import com.example.completablefuture.dto.Post;
import com.example.completablefuture.dto.Todo;
import com.example.completablefuture.dto.UserInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface UserService {

    public UserInfo getUserInfo(String userId);

    public List<Post> postByUser(String userId);

    public List<Comment> commentsByUser(String userId);

    public List<Comment> commentsOnPost(String postId);

    public UserInfo prepapreUserInfo(String userId);

    public UserInfo prepareUserInfoUsingCompletableFuture(String userId) throws ExecutionException, InterruptedException;

    public List<Todo> todosByUser(String userId);
}
