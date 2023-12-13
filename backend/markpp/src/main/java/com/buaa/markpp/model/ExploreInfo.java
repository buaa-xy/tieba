package com.buaa.markpp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExploreInfo {
    private List<PostInfo> postList;
    private List<UserInfo> userList;
}
