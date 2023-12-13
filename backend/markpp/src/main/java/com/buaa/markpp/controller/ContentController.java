package com.buaa.markpp.controller;

import com.buaa.markpp.entity.*;
import com.buaa.markpp.model.*;
import com.buaa.markpp.service.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Validated
@RestController
public class ContentController {
    private final PostService postService;
    private final BlockService blockService;
    private final UserService userService;
    private final CommentService commentService;
    private final AnnotationService annotationService;
    private final VoteService voteService;
    private final ReplyService replyService;
    private final FollowService followService;
    private final LikeService likeService;
    private final FavorService favorService;

    public ContentController(PostService postService, BlockService blockService, UserService userService,
                             CommentService commentService, AnnotationService annotationService,
                             VoteService voteService, ReplyService replyService,
                             FollowService followService, LikeService likeService,
                             FavorService favorService) {
        this.postService = postService;
        this.blockService = blockService;
        this.userService = userService;
        this.commentService = commentService;
        this.annotationService = annotationService;
        this.voteService = voteService;
        this.replyService = replyService;
        this.followService = followService;
        this.likeService = likeService;
        this.favorService = favorService;
    }

    @PostMapping("/compose")
    @Validated(PostInfo.ComposeGroup.class)
    public Result<Void> compose(@RequestBody @Valid PostInfo postInfo,
                                HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            throw new MarkppException(ExceptionType.UNAUTHORIZED);
        }
        Post post = new Post();
        post.setAuthorId(userId);
        post.setTitle(postInfo.getTitle());
        post.setAnnotationAllowed(postInfo.isAnnotationAllowed());
        postService.addPost(post);
        BlockTree root  = postService.divide(postInfo.getContent(), postInfo.isAnnotationAllowed());
        blockService.addBlocks(post.getPostId(), root, postInfo.getContent());
        return new Result<>();
    }

    @PostMapping("/modify/post")
    @Validated(PostInfo.ModifyGroup.class)
    public Result<Void> modify(@RequestBody @Valid PostInfo postInfo,
                               HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            throw new MarkppException(ExceptionType.UNAUTHORIZED);
        }
        Post post = postService.getPost(postInfo.getPostId());
        if (post == null) {
            throw new MarkppException(ExceptionType.NOT_FOUND);
        }
        if (post.getAuthorId().equals(userId)) {
            throw new MarkppException(ExceptionType.UNAUTHORIZED);
        }
        boolean change = postInfo.isAnnotationAllowed() == post.isAnnotationAllowed();
        post.setAnnotationAllowed(postInfo.isAnnotationAllowed());
        post.setTitle(postInfo.getTitle());
        postService.update(post);
        BlockTree root  = postService.divide(postInfo.getContent(), postInfo.isAnnotationAllowed());
        blockService.modifyBlocks(postInfo.getPostId(), root, postInfo.getContent(), change);
        return new Result<>();
    }

    @GetMapping("/post")
    public Result<HashMap<String, Object>> post(@RequestParam(value = "postId") int postId) {
        HashMap<String, Object> map = new HashMap<>();
        Post post = postService.getPost(postId);
        if (post == null) {
            throw new MarkppException(ExceptionType.NOT_FOUND);
        }

        // post
        PostInfo postInfo = postService.makePostInfo(post);
        Account account = userService.getAccountById(post.getAuthorId());

        // get blocks
        List<Block> blocks = blockService.getBlocks(postId);
        List<BlockUnit> blockUnits = new ArrayList<>();

        // set annotations
        for (Block block: blocks) {
            List<Annotation> annotations = annotationService.getAnnotationsByBlockId(block.getBlockId());
            List<AnnotationInfo> annotationInfos = new ArrayList<>();
            for (Annotation annotation: annotations) {
                AnnotationInfo annotationInfo = new AnnotationInfo();
                annotationInfo.setAnnotationId(annotation.getAnnotationId());
                annotationInfo.setContent(annotation.getContent());
                annotationInfo.setTime(annotation.getTime().getTime() / 1000);
                if (annotation.getReplyToId() > 0) {
                    Annotation replyTo = annotationService.getAnnotation(annotation.getReplyToId());
                    annotationInfo.setReplyToUserId(replyTo.getAnnotatorId());
                    Account annotator = userService.getAccountById(replyTo.getAnnotatorId());
                    if (annotator.getNickname() != null) {
                        annotationInfo.setReplyToUserNickname(annotator.getNickname());
                    } else {
                        annotationInfo.setReplyToUserNickname(annotator.getUsername());
                    }
                }
                Account annotator = userService.getAccountById(annotation.getAnnotatorId());
                annotationInfo.setUserId(annotator.getAccountId());
                if (annotator.getNickname() == null) {
                    annotationInfo.setNickname(account.getUsername());
                } else {
                    annotationInfo.setNickname(account.getNickname());
                }
                annotationInfos.add(annotationInfo);
            }
            blockUnits.add(new BlockUnit(block, annotationInfos));
        }

        // set blocks
        postInfo.setBlockUnits(blockUnits);

        // comments
        List<Comment> comments = commentService.selectByPostId(postId);

        List<CommentInfo> commentInfos = new ArrayList<>();
        for (Comment comment: comments) {
            CommentInfo commentInfo = new CommentInfo();
            commentInfo.setCommentId(comment.getCommentId());
            commentInfo.setContent(comment.getContent());
            Account commentator = userService.getAccountById(comment.getCommentatorId());
            commentInfo.setUserId(commentator.getAccountId());
            if (commentator.getNickname() == null) {
                commentInfo.setNickname(commentator.getUsername());
            } else  {
                commentInfo.setNickname(commentator.getNickname());
            }
            commentInfo.setTime(comment.getTime().getTime() / 1000);
            commentInfo.setAvatar(commentator.getAvatar());

            int votes = voteService.sum(comment.getCommentId());

            commentInfo.setVotes(votes);

            List<Reply> replies = replyService.selectByCmtId(comment.getCommentId());
            List<ReplyInfo> replyInfos = new ArrayList<>();
            for (Reply reply: replies) {
                ReplyInfo replyInfo = new ReplyInfo();
                replyInfo.setReplyId(reply.getReplyId());
                int replyToUserId = reply.getReplyToUserId();
                if (replyToUserId > 0) {
                    Account replyToAccount = userService.getAccountById(replyToUserId);
                    replyInfo.setReplyToUserId(replyToAccount.getAccountId());
                    if (replyToAccount.getNickname() == null) {
                        replyInfo.setReplyToNickname(replyToAccount.getUsername());
                    } else {
                        replyInfo.setReplyToNickname(replyToAccount.getNickname());
                    }
                }
                Account replier = userService.getAccountById(reply.getReplierId());
                replyInfo.setUserId(replier.getAccountId());
                if (replier.getNickname() == null) {
                    replyInfo.setNickname(replier.getUsername());
                } else {
                    replyInfo.setNickname(replier.getNickname());
                }
                replyInfo.setTime(reply.getTime().getTime() / 1000);
                replyInfo.setContent(reply.getContent());
                replyInfo.setAvatar(replier.getAvatar());
                replyInfos.add(replyInfo);
            }
            commentInfo.setReplies(replyInfos);

            Vote  vote = voteService.selectByCommentAndUser(comment.getCommentId(), account.getAccountId());
            if (vote != null) {
                commentInfo.setVoteValue(vote.getValue());
            }

            commentInfos.add(commentInfo);
        }
        map.put("comments", commentInfos);

        Like like = likeService.selectByPostAndUser(postId, account.getAccountId());
        if (like != null) {
            postInfo.setLikeValue(like.getValue());
        }

        Favor favor = favorService.selectByPostAndUser(postId, account.getAccountId());
        postInfo.setFavored(favor != null);

        map.put("post", postInfo);

        return new Result<HashMap<String, Object>>().withData(map);
    }

    @GetMapping("/post/content")
    public Result<String> getPostContent(@RequestParam(value = "postId") int postId) {
        Post post = postService.getPost(postId);
        if (post == null) {
            throw new MarkppException(ExceptionType.NOT_FOUND);
        }

        // get blocks
        List<Block> blocks = blockService.getBlocks(postId);
        StringBuilder stringBuilder = new StringBuilder();
        for (Block block: blocks) {
            stringBuilder.append(block.getContent());
        }
        return new Result<String>().withData(stringBuilder.toString());
    }

    @GetMapping("/search")
    public Result<Object> search(@RequestParam(value = "keyword") String keyword) {
        List<Post> posts = postService.search(keyword);
        List<PostInfo> results = new ArrayList<>();
        for (Post post: posts) {
            PostInfo postInfo = postService.makePostInfo(post);
            results.add(postInfo);
        }
        return new Result<>().withData(results);
    }

    @GetMapping("/explore")
    public Result<ExploreInfo> explore(@RequestParam(value = "sort") String sort,
                                       @RequestParam(value = "count") int count) {
        List<Post> posts;
        if (sort.equals("heat"))
            posts = postService.getPostsByHeat(count);
        else if (sort.equals("time"))
            posts = postService.getPostsByTime(count);
        else
            throw new MarkppException(ExceptionType.INVALID_PARAM);
        List<Account> accounts = userService.getAccountByHeat(count);

        List<PostInfo> postInfoList = new ArrayList<>();
        for (Post post: posts) {
            PostInfo postInfo = postService.makePostInfo(post);
            postInfoList.add(postInfo);
        }

        List<UserInfo> userInfos = new ArrayList<>();
        for (Account account: accounts) {
            UserInfo user = new UserInfo();
            user.setUserId(account.getAccountId());
            user.setUsername(account.getUsername());
            user.setEmail(account.getEmail());
            user.setNickname(account.getNickname());
            user.setAvatar(account.getAvatar());
            user.setFans(followService.countFans(account.getAccountId()));
            userInfos.add(user);
        }

        ExploreInfo exploreInfo = new ExploreInfo(postInfoList, userInfos);
        return new Result<ExploreInfo>().withData(exploreInfo);
    }

}