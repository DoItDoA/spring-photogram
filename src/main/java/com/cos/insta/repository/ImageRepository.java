package com.cos.insta.repository;

import com.cos.insta.domain.image.Image;
import com.cos.insta.repository.imageQuerydsl.ImageRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImageRepository extends JpaRepository<Image, Integer> , ImageRepositoryCustom {


}
