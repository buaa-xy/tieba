package com.buaa.markpp.model;

import com.buaa.markpp.validation.NullOrNotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于注册登录和用户信息传参
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    public interface AccountGroup { }

    public interface ProfileGroup { }

    @Positive(message = "用户 ID 必须大于零", groups = ProfileGroup.class)
    // @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer userId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean active;

    @NotNull(message = "用户名不能为 null", groups = AccountGroup.class)
    @NotBlank(message = "用户名不能为空白", groups = AccountGroup.class)
    @Pattern(regexp = "^[\\dA-Za-z]+$", message = "用户名只能包含数字与字母", groups = AccountGroup.class)
    private String username;

    @NotNull(message = "密码不能为空", groups = AccountGroup.class)
    @Length(min = 6, max = 32, message = "密码长度应为 6-32 位",
            groups = {AccountGroup.class, ProfileGroup.class})
    @Pattern(regexp = "^(?!\\d+$)(?![a-zA-Z]+$).+$",
            message = "密码必须同时包含数字与字母",
            groups = {AccountGroup.class, ProfileGroup.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NullOrNotBlank(message = "昵称不能空白", groups = ProfileGroup.class)
    private String nickname;

    @Email(message = "邮箱格式错误", groups = ProfileGroup.class)
    private String email;

    @Pattern(regexp = "^[\\da-z]{32}$", message = "头像图片资源格式错误", groups = ProfileGroup.class)
    private String avatar;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int follows = -1;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int fans = -1;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int likes = -1;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean followed;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<PostInfo> posts = new ArrayList<>();
}
