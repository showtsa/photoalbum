package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.repository.AlbumRepository;
import com.squarecross.photoalbum.repository.PhotoRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class AlbumServiceTest {

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    AlbumService albumService;

    @Test
    void getAlbum() {
        Album album = new Album();
        album.setAlbumName("테스트");
        Album savedAlbum = albumRepository.save(album);

        AlbumDto resAlbum = albumService.getAlbum(savedAlbum.getAlbumId());
        resAlbum.setCount(photoRepository.countByAlbum_AlbumId(savedAlbum.getAlbumId()));
        assertEquals("테스트", resAlbum.getAlbumName());
    }

    @Test
    void testPhotoCount() {
        Album album = new Album();
        album.setAlbumName("테스트");
        Album savedAlbum = albumRepository.save(album);

        Photo photo1 = new Photo();
        photo1.setFileName("사진1");
        photo1.setAlbum(savedAlbum);
        photoRepository.save(photo1);

        Photo photo2 = new Photo();
        photo2.setFileName("사진2");
        photo2.setAlbum(savedAlbum);
        photoRepository.save(photo2);

        AlbumDto resAlbum = albumService.getAlbum(savedAlbum.getAlbumId());
        resAlbum.setCount(photoRepository.countByAlbum_AlbumId(savedAlbum.getAlbumId()));
        assertEquals(2, resAlbum.getCount());
    }

    @Test
    void getAlbumByAlbumName() {
        Album album = new Album();
        album.setAlbumName("테스트2");
        Album savedAlbum = albumRepository.save(album);

        AlbumDto resAlbum = albumService.getAlbumByAlbumName("테스트2");
        assertEquals("테스트2", resAlbum.getAlbumName());

    }

    @Test
    void getAlbumByAlbumName_ExceptionCase() {
        Album album = new Album();
        album.setAlbumName("테스트2");
        Album savedAlbum = albumRepository.save(album);

        assertThrows(EntityNotFoundException.class, () -> {
            albumService.getAlbumByAlbumName("테스트3");
        });
    }

    @Test
    void testAlbumRepository() throws InterruptedException {
        Album album1 = new Album();
        Album album2 = new Album();
        album1.setAlbumName("aaaa");
        album2.setAlbumName("aaab");

        albumRepository.save(album1);
        TimeUnit.SECONDS.sleep(1);
        albumRepository.save(album2);

        List<Album> resDate = albumRepository.findByAlbumNameContainingOrderByCreatedAtDesc("aaa");
        assertEquals("aaab", resDate.get(0).getAlbumName());
        assertEquals("aaaa", resDate.get(1).getAlbumName());
        assertEquals(2, resDate.size());

        List<Album> resName = albumRepository.findByAlbumNameContainingOrderByAlbumNameAsc("aaa");
        assertEquals("aaab", resDate.get(0).getAlbumName());
        assertEquals("aaaa", resDate.get(1).getAlbumName());
        assertEquals(2, resDate.size());
    }

    @Test
    void testChangeAlbumName() throws Exception {
        AlbumDto albumDto = new AlbumDto();
        albumDto.setAlbumName("변경 전");
        AlbumDto res = albumService.createAlbum(albumDto);

        Long albumId = res.getAlbumId();
        AlbumDto updateDto = new AlbumDto();
        updateDto.setAlbumName("변경 후");
        albumService.changeName(albumId, updateDto);

        AlbumDto updatedDto = albumService.getAlbum(albumId);

        assertEquals("변경 후", updatedDto.getAlbumName());
    }

    @Test
    void testDeleteAlbum() throws Exception {
        AlbumDto albumDto = new AlbumDto();
        albumDto.setAlbumName("테스트앨범");
        AlbumDto res = albumService.createAlbum(albumDto);

        assertEquals("테스트앨범", res.getAlbumName());

        Long albumId = res.getAlbumId();
        albumService.deleteAlbum(albumId);

        assertThrows(EntityNotFoundException.class, () -> {
            albumService.getAlbum(albumId);
        });
    }
}