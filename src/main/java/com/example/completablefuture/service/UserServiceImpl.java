package com.example.completablefuture.service;

import com.example.completablefuture.dto.Comment;
import com.example.completablefuture.dto.Post;
import com.example.completablefuture.dto.UserInfo;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.example.completablefuture.util.CommonUtil.delay;

public class UserServiceImpl implements UserService {

    private String API_LOCAL = "https://jsonplaceholder.typicode.com/";
    private WebClient webClient = WebClient.create("https://jsonplaceholder.typicode.com/");

    public UserInfo getUserInfo(String userId) {
        String url = API_LOCAL.concat("/users/").concat(userId);
        UserInfo userInfo = webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(UserInfo.class)
                .block();
        return userInfo;
    }

    public List<Post> postByUserFromApi(String userId) {
        String postUrl = API_LOCAL.concat("/posts/").concat(userId);
        List<Post> posts =
                webClient
                        .get()
                        .uri(postUrl)
                        .retrieve()
                        .bodyToFlux(Post.class)
                        .collectList()
                        .block();
        delay(5000);
        return posts;
    }

    public List<Comment> commentsByUserFromApi(String userId) {
        String commentUrl = API_LOCAL.concat("/comments/").concat(userId);
        List<Comment> result =
                webClient
                        .get()
                        .uri(commentUrl)
                        .retrieve().bodyToFlux(Comment.class)
                        .collectList()
                        .block();
        delay(5000);
        return result;
    }

    public List<Comment> commentsOnPost(String postId) {
        String commentUrl = API_LOCAL.concat("/posts/").concat(postId).concat("/comments");
        List<Comment> result =
                webClient
                        .get()
                        .uri(commentUrl)
                        .retrieve()
                        .bodyToFlux(Comment.class)
                        .collectList()
                        .block();
        delay(5000);
        return result;
    }

    public UserInfo prepapreUserInfo(String userId) {
        UserInfo userInfo = getUserInfo(userId);
        List<Post> postList = postByUserFromApi(userId);

        postList.forEach(post -> {
            List<Comment> comments = commentsOnPost(post.getId());
            post.setComments(comments);
        });
        userInfo.setPosts(postList);

        return userInfo;

    }

    public UserInfo prepareUserInfoUsingCompletableFuture(String userId) throws ExecutionException, InterruptedException {

        UserInfo userInfo = CompletableFuture.supplyAsync(() -> getUserInfo(userId)).get();
        List<Post> userPosts = CompletableFuture.supplyAsync(() -> postByUserFromApi(userId)).get();

        userPosts.stream().forEach(post -> {
            try {
                List<Comment> commentList = CompletableFuture.supplyAsync(() -> commentsOnPost(post.getId())).get();
                post.setComments(commentList);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });


        userInfo.setPosts(userPosts);
        return userInfo;
    }

}
