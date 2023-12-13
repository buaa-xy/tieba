package com.buaa.markpp.controller;

import com.buaa.markpp.entity.Account;
import com.buaa.markpp.entity.Follow;
import com.buaa.markpp.entity.Post;
import com.buaa.markpp.model.*;
import com.buaa.markpp.service.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Validated
@RestController
public class UserController {

    private final UserService userService;
    private final ImageService imageService;

    private final PostService postService;

    private final FollowService followService;

    private final LikeService likeService;

    public UserController(UserService userService, ImageService imageService, PostService postService, FollowService followService, LikeService likeService) {
        this.userService = userService;
        this.imageService = imageService;
        this.postService = postService;
        this.followService = followService;
        this.likeService = likeService;
    }

    @PostMapping("/register")
    @Validated(UserInfo.AccountGroup.class)
    public Result<Void> register(@RequestBody @Valid UserInfo userInfo) {
        userService.addUser(userInfo);
        return new Result<>();
    }

    @PostMapping("/login")
    @Validated(UserInfo.AccountGroup.class)
    public Result<Void> login(@RequestBody @Valid UserInfo userInfo, HttpSession session) {
        String username = userInfo.getUsername();
        String password = userInfo.getPassword();
        Account account = userService.getAccountByName(username);
        if (!account.getPassword().equals(password)) {
            throw new MarkppException("密码错误");
        }
        session.setAttribute("userId", account.getAccountId());
        return new Result<>();
    }

    @PostMapping("/logout")
    public Result<Void> logout(HttpSession session) {
        session.invalidate();
        return new Result<>();
    }

    @GetMapping("/profile")
    public Result<UserInfo> profile(
            @RequestParam(value = "userId", required = false) @Positive(message = "用户 ID 应为正数") Integer userId,
            @RequestParam(value = "detailed", required = false) boolean detailed,
            HttpSession session) {
        Result<UserInfo> result = new Result<>();
        boolean selfQuery = false;
        if (userId == null) {
            selfQuery = true;
            userId = (Integer) session.getAttribute("userId");
        }
        if (userId == null) {
            UserInfo user = new UserInfo();
            user.setActive(false);
            return result.withData(user);
        }
        Account account = userService.getAccountById(userId);
        UserInfo user = new UserInfo();
        if (selfQuery) {
            user.setActive(true);
        }
        user.setUserId(userId);
        user.setUsername(account.getUsername());
        user.setEmail(account.getEmail());
        user.setNickname(account.getNickname());
        user.setAvatar(account.getAvatar());
        if (detailed) {
            user.setFans(followService.countFans(userId));
            user.setFollows(followService.countFollows(userId));
            user.setLikes(likeService.sumByAuthor(userId));
            List<Post> postList = postService.getPostByAccountId(userId);
            for (Post post : postList) {
                user.getPosts().add(postService.makePostInfo(post));
            }
        }
        if (!selfQuery) {
            Integer selfId = (Integer) session.getAttribute("userId");
            if (selfId != null) {
                Follow follow = followService.selectByAuthorAndFollower(userId, selfId);
                user.setFollowed(follow != null);
            }
        }
        return result.withData(user);
    }

    @PostMapping("/modify/profile")
    @Validated(UserInfo.ProfileGroup.class)
    public Result<Void> modify(@RequestBody @Valid UserInfo userInfo, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            throw new MarkppException(ExceptionType.UNAUTHORIZED);
        }
        if (userInfo.getAvatar() != null) {
            if (!imageService.existsToken(userInfo.getAvatar())) {
                throw new MarkppException(ExceptionType.NOT_FOUND);
            }
        }
        userInfo.setUserId(userId);
        userService.update(userInfo);
        return new Result<>();
    }

    @GetMapping("/follow/all")
    public Result<List<UserInfo>> follows(
            @RequestParam(value = "userId", required = false) @Positive(message = "用户 ID 应为正数") Integer userId,
            HttpSession session) {
        Result<List<UserInfo>> result = new Result<>();
        List<UserInfo> data = new ArrayList<>();
        boolean selfQuery = false;
        if (userId == null) {
            selfQuery = true;
            userId = (Integer) session.getAttribute("userId");
        }
        if (userId == null) {
            UserInfo user = new UserInfo();
            user.setActive(false);
            return result.withFailure(ExceptionType.UNAUTHORIZED);
        }
        int[] ids = followService.getFolloweeIdsByFollowerId(userId, selfQuery ? 114514 : 20);
        for (int id : ids) {
            Account account = userService.getAccountById(id);
            UserInfo user = new UserInfo();
            user.setUserId(id);
            user.setUsername(account.getUsername());
            user.setEmail(account.getEmail());
            user.setNickname(account.getNickname());
            user.setAvatar(account.getAvatar());
            data.add(user);
        }
        return result.withData(data);
    }

    @GetMapping("/favorites")
    public Result<List<PostInfo>> favorites(HttpSession session) {
        Result<List<PostInfo>> result = new Result<>();
        List<PostInfo> data = new ArrayList<>();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return result.withFailure(ExceptionType.UNAUTHORIZED);
        }
        List<Post> postList = postService.getFavoritePosts(userId);
        for (Post post : postList) {
            data.add(postService.makePostInfo(post));
        }
        return result.withData(data);
    }
}
