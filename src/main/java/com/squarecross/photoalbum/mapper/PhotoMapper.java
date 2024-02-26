package com.squarecross.photoalbum.mapper;

import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.PhotoDto;

public class PhotoMapper {

    public static PhotoDto convertToDto(Photo photo) {
        PhotoDto photoDto = new PhotoDto();
        photoDto.setPhotoId(photo.getPhotoId());
        photoDto.setFileName(photo.getFileName());
        photoDto.setThumbUrl(photo.getThumbUrl());
        photoDto.setOriginalUrl(photo.getOriginalUrl());
        photoDto.setUploadedAt(photo.getUploadedAt());
        photoDto.setFileSize(photo.getFileSize());
        if(photo.getAlbum() != null) {
            photoDto.setAlbumId(photo.getAlbum().getAlbumId());
        }
        return photoDto;
    }
    public static Photo convertToModel(PhotoDto photoDto) {
        Photo photo = new Photo();
        photo.setPhotoId(photoDto.getPhotoId());
        photo.setFileName(photoDto.getFileName());
        photo.setThumbUrl(photoDto.getThumbUrl());
        photo.setOriginalUrl(photoDto.getOriginalUrl());
        photo.setUploadedAt(photoDto.getUploadedAt());
        photo.setFileSize(photo.getFileSize());

        return photo;
    }
}
