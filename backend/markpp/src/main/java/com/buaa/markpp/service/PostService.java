package com.buaa.markpp.service;

import com.buaa.markpp.entity.Post;
import com.buaa.markpp.model.BlockTree;
import com.buaa.markpp.model.PostInfo;

import java.util.List;

public interface PostService {
    void addPost(Post post);
    BlockTree divide(String postContent, boolean annotationAllowed);
    Post getPost(int postId);
    List<Post> getPostByAccountId(int accountId);
    PostInfo makePostInfo(Post post);
    void update(Post post);
    List<Post> search(String keyword);
    List<Post> getPostsByHeat(int number);
    List<Post> getPostsByTime(int number);

    List<Post> getFavoritePosts(int userId);
}
