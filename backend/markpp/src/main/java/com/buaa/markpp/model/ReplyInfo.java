package com.buaa.markpp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReplyInfo {
    public interface reply {}

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int replyId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Positive(message = "ID 必须大于零", groups = ReplyInfo.reply.class)
    private int commentId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int replyToId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int replyToUserId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String replyToNickname;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int userId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String nickname;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String avatar;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long time;

    @NotNull(message = "内容不能为 null", groups = ReplyInfo.reply.class)
    @NotBlank(message = "内容不能为空白", groups = ReplyInfo.reply.class)
    private String content;
}
