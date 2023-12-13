package com.buaa.markpp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentInfo {
    @Positive(message = "帖子 ID 必须大于0")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int postId;

    @NotNull(message = "评论内容不能为 null")
    @NotBlank(message = "评论内容不能为空白")
    private String content;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int commentId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int userId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String nickname;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String avatar;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long time;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int votes;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<ReplyInfo> replies;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer voteValue;
}
