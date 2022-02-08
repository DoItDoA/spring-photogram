package com.cos.insta.web.dto.hashtag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HashtagDto {
    private String name;
    private int imageId;
    private Timestamp createDate;
}
