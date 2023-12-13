package com.buaa.markpp.model;

import lombok.*;

import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VoteOrLikeInfo {
    @Positive(message = "目标 ID 必须大于0")
    int id;

    int value;
}
