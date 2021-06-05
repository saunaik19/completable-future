package com.example.completablefuture.resources;

import com.example.completablefuture.dto.Comment;
import com.example.completablefuture.dto.Post;
import com.example.completablefuture.dto.UserInfo;
import com.example.completablefuture.service.UserService;
import com.example.completablefuture.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.completablefuture.util.CommonUtil.*;

@RestController
public class UserResource {

    //private ThreadLocal<StringBuffer> API_URL = new ThreadLocal<>()new String("https://jsonplaceholder.typicode.com/");

    @Autowired
    private UserService userService;

    @GetMapping("/users/{userId}")
    public UserInfo userInfo(@PathVariable("userId") String userId) {
        startTimer();
        UserInfo userInfo = userService.getUserInfo(userId);
        timeTaken();
        stopWatchReset();
        return userInfo;
    }

    @GetMapping("/posts/{userId}")
    public ResponseEntity<List<Post>> postByUser(@PathVariable("userId") String userId) {
        List<Post> result = userService.postByUserFromApi(userId);
        startTimer();
        ResponseEntity<List<Post>> responseEntity =
                new ResponseEntity(result, HttpStatus.OK);
        timeTaken();
        stopWatchReset();
        return responseEntity;
    }

    @GetMapping("/comments/{userId}")
    public ResponseEntity<List<Comment>> commentsByUser(@PathVariable("userId") String userId) {
        List<Comment> result = userService.commentsByUserFromApi(userId);
        startTimer();
        ResponseEntity<List<Comment>> responseEntity =
                new ResponseEntity(result, HttpStatus.OK);
        timeTaken();
        stopWatchReset();
        return responseEntity;
    }


    @GetMapping("/users/allData/{userId}")
    public UserInfo allUserInfo(@PathVariable("userId") String userId) {

        UserInfo userInfoT = null;
        UserInfo userInfo = null;
        try {
            startTimer();
            LoggerUtil.log("Sada call....");
            userInfo = userService.prepapreUserInfo(userId);
            timeTaken();
            stopWatchReset();
            startTimer();
            userInfoT = userService.prepareUserInfoUsingCompletableFuture(userId);
            LoggerUtil.log("Threaded call....");
            timeTaken();
            stopWatchReset();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return userInfoT;
    }

    @GetMapping("/users/allDataT/{userId}")
    public UserInfo allUserInfoT(@PathVariable("userId") String userId) {

        UserInfo userInfoT = null;
        UserInfo userInfo = null;
        try {
            startTimer();
            userInfoT = userService.prepareUserInfoUsingCompletableFuture(userId);
            LoggerUtil.log("Threaded call....");
            timeTaken();
            stopWatchReset();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return userInfoT;
    }


}
