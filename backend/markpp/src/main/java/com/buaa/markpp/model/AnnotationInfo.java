package com.buaa.markpp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AnnotationInfo {

    @Positive(message = "block ID 必须大于零")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int blockId;

    @NotNull(message = "注释内容不能为 null")
    @NotBlank(message = "注释内容不能为空白")
    private String content;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer replyToId;

    private Integer replyToUserId;

    private String replyToUserNickname;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int annotationId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int userId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String nickname;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long time;
}
