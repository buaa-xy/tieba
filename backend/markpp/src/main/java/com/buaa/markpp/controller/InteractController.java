package com.buaa.markpp.controller;

import com.buaa.markpp.entity.*;
import com.buaa.markpp.model.*;
import com.buaa.markpp.service.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

@Validated
@RestController
public class InteractController {
    private final PostService postService;
    private final BlockService blockService;
    private final AnnotationService annotationService;
    private final CommentService commentService;
    private final VoteService voteService;
    private final LikeService likeService;
    private final UserService userService;
    private final FollowService followService;
    private final FavorService favorService;
    private final ReplyService replyService;

    public InteractController(PostService postService, BlockService blockService,
                              AnnotationService annotationService, CommentService commentService,
                              VoteService voteService, LikeService likeService,
                              UserService userService, FollowService followService,
                              FavorService favorService, ReplyService replyService) {
        this.postService = postService;
        this.blockService = blockService;
        this.annotationService = annotationService;
        this.commentService = commentService;
        this.voteService = voteService;
        this.likeService = likeService;
        this.userService = userService;
        this.followService = followService;
        this.favorService = favorService;
        this.replyService = replyService;
    }

    @PostMapping("/annotate")
    public Result<Void> annotate(@RequestBody @Valid AnnotationInfo annotationInfo, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            throw new MarkppException(ExceptionType.UNAUTHORIZED);
        }
        int blockId = annotationInfo.getBlockId();
        Block block = blockService.getBlock(blockId);
        if (block == null) {
            throw new MarkppException(ExceptionType.NOT_FOUND);
        }
        Annotation annotation = new Annotation();
        if (annotationInfo.getReplyToId() != null) {
            int replyToId = annotationInfo.getReplyToId();
            if (replyToId > 0) {
                Annotation annotationReplied = annotationService.getAnnotation(replyToId);
                if (annotationReplied == null) {
                    throw new MarkppException(ExceptionType.NOT_FOUND);
                }
                if (annotationReplied.getBlockId() != blockId) {
                    throw new MarkppException(ExceptionType.UNAUTHORIZED);
                }
            }
            annotation.setReplyToId(replyToId);
        }
        annotation.setContent(annotationInfo.getContent());
        annotation.setBlockId(blockId);
        annotation.setAnnotatorId(userId);
        annotationService.addAnnotation(annotation);
        return new Result<>();
    }

    @PostMapping("/comment")
    public Result<Void> comment(@RequestBody @Valid CommentInfo commentInfo, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            throw new MarkppException(ExceptionType.UNAUTHORIZED);
        }
        int postId = commentInfo.getPostId();
        String content = commentInfo.getContent();
        Post post = postService.getPost(postId);
        if (post == null) {
            throw new MarkppException(ExceptionType.NOT_FOUND);
        }
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPostId(postId);
        comment.setCommentatorId(userId);
        commentService.addComment(comment);
        return new Result<>();
    }

    @PostMapping("/vote")
    public Result<Void> vote(@RequestBody @Valid VoteOrLikeInfo voteInfo, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            throw new MarkppException(ExceptionType.UNAUTHORIZED);
        }
        int commentId = voteInfo.getId();
        Comment comment = commentService.getComment(commentId);
        if (comment == null)
            throw new MarkppException(ExceptionType.NOT_FOUND);

        Vote vote = voteService.selectByCommentAndUser(commentId, userId);
        if (vote != null) {
            voteService.delete(vote.getVoteId());
        }
        if (vote == null || vote.getValue() != voteInfo.getValue()) {
            vote = new Vote();
            vote.setVoterId(userId);
            vote.setCommentId(commentId);
            vote.setValue(voteInfo.getValue());
            voteService.addVote(vote);
        }
        return new Result<>();
    }

    @PostMapping("/like")
    public Result<Void> like(@RequestBody @Valid VoteOrLikeInfo likeInfo, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            throw new MarkppException(ExceptionType.UNAUTHORIZED);
        }
        int postId = likeInfo.getId();
        Post post = postService.getPost(postId);
        if (post == null)
            throw new MarkppException(ExceptionType.NOT_FOUND);

        Like like = likeService.selectByPostAndUser(postId, userId);
        if (like != null) {
            likeService.delete(like.getLikeId());
        }
        if (like == null || like.getValue() != likeInfo.getValue()) {
            like = new Like();
            like.setVoterId(userId);
            like.setPostId(postId);
            like.setValue(likeInfo.getValue());
            likeService.add(like);
        }
        return new Result<>();
    }

    @PostMapping("/follow")
    public Result<Void> follow(@RequestBody Map<String, String> request, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            throw new MarkppException(ExceptionType.UNAUTHORIZED);
        }
        String username = request.get("username");
        if (username == null)
            throw new MarkppException(ExceptionType.INVALID_PARAM);
        Account account = userService.getAccountByName(username);
        if (account == null)
            throw new MarkppException(ExceptionType.NOT_FOUND);

        if (account.getAccountId() == userId)
            throw new MarkppException(ExceptionType.UNAUTHORIZED);

        Follow follow = followService.selectByAuthorAndFollower(account.getAccountId(), userId);
        if (follow != null)
            throw new MarkppException(ExceptionType.REPEATED_OPERATION);
        follow = new Follow();
        follow.setAuthorId(account.getAccountId());
        follow.setFollowerId(userId);
        followService.add(follow);
        return new Result<>();
    }

    @PostMapping("/follow/cancel")
    public Result<Void> cancelFollow(@RequestBody Map<String, String> request, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            throw new MarkppException(ExceptionType.UNAUTHORIZED);
        }
        String username = request.get("username");
        if (username == null)
            throw new MarkppException(ExceptionType.INVALID_PARAM);
        Account account = userService.getAccountByName(username);
        if (account == null)
            throw new MarkppException(ExceptionType.NOT_FOUND);
        Follow follow = followService.selectByAuthorAndFollower(account.getAccountId(), userId);
        if (follow == null) {
            throw new MarkppException(ExceptionType.NOT_FOUND);
        }
        followService.delete(follow.getFollowId());
        return new Result<>();
    }

    @PostMapping("/favor")
    public Result<Void> favor(@RequestBody Map<String, Integer> request, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            throw new MarkppException(ExceptionType.UNAUTHORIZED);
        }
        Integer postId = request.get("postId");
        if (postId == null) {
            throw new MarkppException(ExceptionType.INVALID_PARAM);
        }
        Post post = postService.getPost(postId);
        if (post == null) {
            throw new MarkppException(ExceptionType.NOT_FOUND);
        }

        Favor favor = favorService.selectByPostAndUser(postId, userId);
        if (favor != null) {
            throw new MarkppException(ExceptionType.REPEATED_OPERATION);
        }

        favor = new Favor();
        favor.setCollectorId(userId);
        favor.setPostId(postId);
        favorService.add(favor);

        return new Result<>();
    }

    @PostMapping("/favor/cancel")
    public Result<Void> cancelFavor(@RequestBody Map<String, Integer> request, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            throw new MarkppException(ExceptionType.UNAUTHORIZED);
        }
        Integer postId = request.get("postId");
        if (postId == null) {
            throw new MarkppException(ExceptionType.INVALID_PARAM);
        }
        Post post = postService.getPost(postId);
        if (post == null) {
            throw new MarkppException(ExceptionType.NOT_FOUND);
        }

        Favor favor = favorService.selectByPostAndUser(postId, userId);
        if (favor == null) {
            throw new MarkppException(ExceptionType.NOT_FOUND);
        }
        favorService.delete(favor.getFavorId());
        return new Result<>();
    }

    @Validated(ReplyInfo.reply.class)
    @PostMapping("/reply")
    public Result<Void> reply(@RequestBody @Valid ReplyInfo replyInfo,
                              HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            throw new MarkppException(ExceptionType.UNAUTHORIZED);
        }
        int commentId = replyInfo.getCommentId();

        Comment comment = commentService.getComment(commentId);
        if (comment == null) {
            throw new MarkppException(ExceptionType.NOT_FOUND);
        }

        Reply reply = new Reply();

        int replyToId = replyInfo.getReplyToId();
        if (replyToId > 0) {
            Reply replyTo = replyService.selectById(replyToId);
            if (replyTo == null) {
                throw new MarkppException(ExceptionType.NOT_FOUND);
            }
            reply.setReplyToUserId(replyTo.getReplierId());
        }

        reply.setReplierId(userId);
        reply.setCommentId(commentId);

        String content = replyInfo.getContent();
        reply.setContent(content);

        replyService.add(reply);
        return new Result<>();
    }

}
