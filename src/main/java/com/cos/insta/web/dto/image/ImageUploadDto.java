package com.cos.insta.web.dto.image;

import com.cos.insta.domain.image.Image;
import com.cos.insta.domain.user.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
public class ImageUploadDto {
    private MultipartFile file; // 변수명은 html의 name과 일치해야한다
    private String caption;
    private String hashtags;

    public Image toEntity(String postImageUrl, User user){
        return Image.builder()
                .caption(caption)
                .postImageUrl(postImageUrl)
                .user(user)
                .build();
    }
}
