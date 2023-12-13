package com.buaa.markpp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostInfo {
    public interface ComposeGroup {}

    public interface PostGroup {}

    public interface ModifyGroup{}

    @NotNull(message = "标题不能为 null",
            groups = {ComposeGroup.class, PostGroup.class, ModifyGroup.class})
    @NotBlank(message = "标题不能为空白",
            groups = {ComposeGroup.class, PostGroup.class, ModifyGroup.class})
    private String title;
    
    @NotNull(message = "帖子内容不能为 null", groups = {ComposeGroup.class, ModifyGroup.class})
    @NotBlank(message = "帖子内容不能为空白", groups = {ComposeGroup.class, ModifyGroup.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String content;

    private boolean annotationAllowed;

    @Positive(message = "帖子 ID 必须大于0", groups = {PostGroup.class, ModifyGroup.class})
    private int postId;

    @Positive(message = "用户 ID 必须大于0", groups = PostGroup.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int userId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String nickname;

    @Pattern(regexp = "^[\\da-z]{32}$", message = "头像图片资源格式错误", groups = PostGroup.class)
    private String avatar;

    @DecimalMin(value = "0", message = "点赞数必须大于等于0", groups = PostGroup.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int likes;

    @DecimalMin(value = "0", message = "评论数必须大于等于0", groups = PostGroup.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int comments;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long postTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<BlockUnit> blockUnits;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean favored;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer likeValue;
}
