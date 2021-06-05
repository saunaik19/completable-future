package com.example.completablefuture.service;

import com.example.completablefuture.dto.Comment;
import com.example.completablefuture.dto.Post;
import com.example.completablefuture.dto.Todo;
import com.example.completablefuture.dto.UserInfo;
import com.example.completablefuture.util.LoggerUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.example.completablefuture.util.CommonUtil.delay;

@Service("userService")
public class UserServiceImpl implements UserService {

    private String API_LOCAL = "https://jsonplaceholder.typicode.com";
    private WebClient webClient = WebClient.create("https://jsonplaceholder.typicode.com/");

    @Override
    public UserInfo getUserInfo(String userId) {
        String url = API_LOCAL.concat("/users/").concat(userId);
        return getUserInfoFromApi(url);
    }

    private UserInfo getUserInfoFromApi(String url) {
        LoggerUtil.log("Calling URL for UserInfo " + url);
        delay(3000);
        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(UserInfo.class)
                .block();
    }

    @Override
    public List<Post> postByUser(String userId) {
        String postUrl = API_LOCAL.concat("/posts/").concat(userId);
        List<Post> posts =
                postByUserFromApi(postUrl);
        return posts;
    }

    private List<Post> postByUserFromApi(String postUrl) {
        LoggerUtil.log("Calling URL for User Posts " + postUrl);
        delay(3000);
        return webClient
                .get()
                .uri(postUrl)
                .retrieve()
                .bodyToFlux(Post.class)
                .collectList()
                .block();
    }

    @Override
    public List<Comment> commentsByUser(String userId) {
        String commentUrl = API_LOCAL.concat("/comments/").concat(userId);
        return CommentsOnPostFromApi(commentUrl);
    }

    @Override
    public List<Comment> commentsOnPost(String postId) {
        String commentUrl = API_LOCAL.concat("/posts/").concat(postId).concat("/comments");
        LoggerUtil.log("calling URL : " + commentUrl);
        return CommentsOnPostFromApi(commentUrl);
    }

    private List<Comment> CommentsOnPostFromApi(String commentUrl) {
        LoggerUtil.log("Calling URL for User Comments :: " + commentUrl);
        delay(3000);
        return webClient
                .get()
                .uri(commentUrl)
                .retrieve()
                .bodyToFlux(Comment.class)
                .collectList()
                .block();
    }

    @Override
    public UserInfo prepapreUserInfo(String userId) {
        UserInfo userInfo = getUserInfo(userId);
        List<Post> postList = postByUser(userId);
        List<Todo> todoList =todosByUser(userId);
        postList.forEach(post -> {
            List<Comment> comments = commentsOnPost(post.getId());
            post.setComments(comments);
        });
        userInfo.setPosts(postList);
        userInfo.setTodos(todoList);
        return userInfo;
    }

    @Override
    public UserInfo prepareUserInfoUsingCompletableFuture(String userId) throws ExecutionException, InterruptedException {

        CompletableFuture<UserInfo> userInfoCF = CompletableFuture.supplyAsync(() -> getUserInfo(userId));
        CompletableFuture<List<Post>> userPostsCF = CompletableFuture.supplyAsync(() -> postByUser(userId));
        CompletableFuture<List<Todo>> userTodosCF = CompletableFuture.supplyAsync(() -> todosByUser(userId));

        UserInfo userInfoCompletableFuture = userInfoCF.thenCombine(userPostsCF, (userInfo, userPosts) ->
                new UserInfo(userInfo.getId(), userInfo, userPosts))
                .thenCombine(userTodosCF,(prev,next)->new UserInfo(prev.getId(),prev,prev.getPosts(),next))
                .join();
        userInfoCompletableFuture.getPosts().forEach(post -> {
            try {
                CompletableFuture<List<Comment>> commentList = CompletableFuture.supplyAsync(() -> commentsOnPost(post.getId()));
                post.setComments(commentList.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        return userInfoCompletableFuture;
    }

    @Override
    public List<Todo> todosByUser(String userId) {
        String todoUrl = API_LOCAL.concat("/users/").concat(userId).concat("/todos/");
        LoggerUtil.log("calling URL : " + todoUrl);
        return todoByUserFromApi(todoUrl);
    }

    private List<Todo> todoByUserFromApi(String url) {
        delay(3000);
        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToFlux(Todo.class)
                .collectList()
                .block();

    }

}
