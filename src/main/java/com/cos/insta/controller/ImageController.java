package com.cos.insta.controller;

import com.cos.insta.config.auth.PrincipalDetails;
import com.cos.insta.handler.ex.CustomValidationException;
import com.cos.insta.service.ImageService;
import com.cos.insta.web.dto.image.ImagePagingDto;
import com.cos.insta.web.dto.image.ImagePopularDto;
import com.cos.insta.web.dto.image.ImageUploadDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class ImageController {

    @Autowired
    ImageService imageService;

    @GetMapping({"/", "image/story"})
    public String story(@RequestParam(required = false) String search, Model model) {
        model.addAttribute("search", search);
        return "image/story";
    }

    @GetMapping("image/myStory/{imageId}")
    public String myStory(@PathVariable int imageId, Model model) {
        model.addAttribute("imageId", imageId);
        return "image/myStory";
    }

    @GetMapping("image/popular")
    public String popular(Model model) {
        List<ImagePopularDto> images = imageService.popularFeed();

        model.addAttribute("images", images);

        return "image/popular";
    }

    @GetMapping("image/upload")
    public String upload() {
        return "image/upload";
    }

    @PostMapping("image/upload")
    public String imageUpload(ImageUploadDto imageUploadDto, @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {
        if (imageUploadDto.getFile().isEmpty()) {
            throw new CustomValidationException("이미지가 첨부되지 않았습니다.", null);
        }
        imageService.ImageUploadS3(imageUploadDto, principalDetails);

        return "redirect:/user/" + principalDetails.getUser().getId();
    }
}
