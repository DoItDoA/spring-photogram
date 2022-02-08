package com.cos.insta.web.dto.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SearchNameDto {
    private int id;
    private String username;
    private String name;
    private String profileImageUrl;
}
