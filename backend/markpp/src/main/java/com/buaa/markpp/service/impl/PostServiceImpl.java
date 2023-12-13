package com.buaa.markpp.service.impl;

import com.buaa.markpp.dao.PostDao;
import com.buaa.markpp.entity.Account;
import com.buaa.markpp.entity.Post;
import com.buaa.markpp.model.BlockTree;
import com.buaa.markpp.model.PostInfo;
import com.buaa.markpp.service.*;
import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.ast.NodeVisitor;
import com.vladsch.flexmark.util.ast.VisitHandler;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private final PostDao postDao;
    static BlockTree currentNode;
    private final LikeService likeService;
    private final CommentService commentService;
    private final UserService userService;

    NodeVisitor visitor = new NodeVisitor(
            new VisitHandler<>(Heading.class, this::visit)
    );

    public void visit(Node node) {
        if (node instanceof Heading) {
            String headingChars = node.getChars().toString();
            int level = headingChars.indexOf(' ');
            String heading = headingChars.substring(level + 1);
            int startOffSet = node.getStartOffset();
            if (currentNode == null) {
                currentNode = new BlockTree();
                if (startOffSet == 0) {
                    currentNode.setHeading(heading);
                    currentNode.setLevel(level);
                    currentNode.setStartOffSet(startOffSet);
                } else {
                    currentNode.setLevel(0);
                    currentNode.setFirstSon(new BlockTree());
                    currentNode.setStartOffSet(0);
                    currentNode.setEndOffSet(startOffSet);
                    BlockTree father = currentNode;
                    currentNode = currentNode.getFirstSon();
                    currentNode.setFather(father);
                    currentNode.setHeading(heading);
                    currentNode.setStartOffSet(startOffSet);
                    currentNode.setLevel(level);
                }
            } else {
                currentNode.setEndOffSet(startOffSet);
                if (level == currentNode.getLevel()) {
                    currentNode.setNextBrother(new BlockTree());
                    currentNode.getNextBrother().setFather(currentNode.getFather());
                    BlockTree brother = currentNode;
                    currentNode = currentNode.getNextBrother();
                    currentNode.setLastBrother(brother);
                } else if (level > currentNode.getLevel()) {
                    currentNode.setFirstSon(new BlockTree());
                    BlockTree father = currentNode;
                    currentNode = currentNode.getFirstSon();
                    currentNode.setFather(father);
                } else {
                    while (currentNode.getFather() != null && level <= currentNode.getFather().getLevel()) {
                        currentNode = currentNode.getFather();
                    }
                    currentNode.setNextBrother(new BlockTree());
                    currentNode.getNextBrother().setFather(currentNode.getFather());
                    BlockTree brother = currentNode;
                    currentNode = currentNode.getNextBrother();
                    currentNode.setLastBrother(brother);
                }
                currentNode.setHeading(heading);
                currentNode.setLevel(level);
                currentNode.setStartOffSet(startOffSet);
            }
        }
        visitor.visitChildren(node);
    }

    public PostServiceImpl(PostDao postDao, LikeService likeService,
                           CommentService commentService, UserService userService) {
        this.postDao = postDao;
        this.likeService = likeService;
        this.commentService = commentService;
        this.userService = userService;
    }

    @Override
    public void addPost(Post post) {
        postDao.insert(post);
    }

    @Override
    public BlockTree divide(String postContent, boolean annotationAllowed) {
        if (annotationAllowed) {
            currentNode = null;
            Parser parser = Parser.builder().build();
            Node document = parser.parse(postContent);
            visit(document);
            currentNode.setEndOffSet(postContent.length());
            return currentNode.getRoot();
        } else {
            BlockTree blockTreeNode = new BlockTree();
            blockTreeNode.setStartOffSet(0);
            blockTreeNode.setEndOffSet(postContent.length());
            return blockTreeNode;
        }
    }

    @Override
    public Post getPost(int postId) {
        return postDao.select(postId);
    }

    @Override
    public List<Post> getPostByAccountId(int accountId) {
        return postDao.selectByAccountId(accountId);
    }

    @Override
    public PostInfo makePostInfo(Post post) {
        PostInfo postInfo = new PostInfo();
        int postId = post.getPostId();
        postInfo.setLikes(likeService.sum(postId));
        postInfo.setComments(commentService.count(postId));
        postInfo.setPostId(postId);
        postInfo.setTitle(post.getTitle());
        postInfo.setAnnotationAllowed(post.isAnnotationAllowed());
        Account account = userService.getAccountById(post.getAuthorId());
        postInfo.setUserId(account.getAccountId());
        if (account.getNickname() == null) {
            postInfo.setNickname(account.getUsername());
        } else {
            postInfo.setNickname(account.getNickname());
        }
        postInfo.setAvatar(account.getAvatar());
        Date date = post.getTime();
        postInfo.setPostTime(date.getTime() / 1000);
        return postInfo;
    }

    @Override
    public void update(Post post) {
        postDao.update(post);
    }

    @Override
    public List<Post> search(String keyword) {
        return postDao.search(keyword);
    }

    @Override
    public List<Post> getPostsByHeat(int number) {
        return postDao.selectSortedByHeat(number);
    }

    @Override
    public List<Post> getPostsByTime(int number) {
        return postDao.selectSortedByTime(number);
    }

    @Override
    public List<Post> getFavoritePosts(int userId) {
        return postDao.selectFavorites(userId);
    }
}
