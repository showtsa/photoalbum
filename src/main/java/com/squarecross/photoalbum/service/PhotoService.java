package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.mapper.PhotoMapper;
import com.squarecross.photoalbum.repository.AlbumRepository;
import com.squarecross.photoalbum.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PhotoService {
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private AlbumRepository albumRepository;

    public PhotoDto getPhoto(long albumId, long photoId) {
        Optional<Photo> res = photoRepository.findByAlbumAlbumIdAndPhotoId(albumId, photoId);
        if(res.isEmpty()) {
            throw new NoSuchElementException(String.format("%d번의 사진첩에서 %d번에 해당하는 사진을 찾을 수 없습니다.", albumId, photoId));
        }

        return PhotoMapper.convertToDto(res.get());
    }

}
