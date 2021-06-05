package com.example.completablefuture.service;

import com.example.completablefuture.dto.Comment;
import com.example.completablefuture.dto.Post;
import com.example.completablefuture.dto.UserInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface UserService {

    public UserInfo getUserInfo(String userId);

    public List<Post> postByUserFromApi(String userId);

    public List<Comment> commentsByUserFromApi(String userId);

    public List<Comment> commentsOnPost(String postId);

    public UserInfo prepapreUserInfo(String userId);

    public UserInfo prepareUserInfoUsingCompletableFuture(String userId) throws ExecutionException, InterruptedException;
}
