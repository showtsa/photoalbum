package com.squarecross.photoalbum.controller;

import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.repository.AlbumRepository;
import com.squarecross.photoalbum.service.PhotoService;
import org.apache.coyote.Response;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/albums/{albumId}/photos")
public class PhotoController {
    @Autowired
    private PhotoService photoService;

    @RequestMapping("/{photoId}")
    public ResponseEntity<PhotoDto> getPhotoInfo(@PathVariable("albumId") final long albumId,
                                                 @PathVariable("photoId") final long photoId) {
        PhotoDto photoDto = photoService.getPhoto(albumId, photoId);
        return new ResponseEntity<>(photoDto, HttpStatus.OK);
    }

    @RequestMapping(value="", method= RequestMethod.POST)
    public ResponseEntity<List<PhotoDto>> uploadPhotos(@PathVariable("albumId") final long albumId,
                                                       @RequestParam("photos") MultipartFile[] files) {
        List<PhotoDto> photos = new ArrayList<>();
        for(MultipartFile file : files) {
            PhotoDto photoDto = photoService.savePhoto(file, albumId);
            photos.add(photoDto);
        }
        return new ResponseEntity<>(photos, HttpStatus.OK);
    }

    @RequestMapping(value="/download", method = RequestMethod.GET)
    public void downloadPhotos(@RequestParam("photoIds") long[] photoIds, HttpServletResponse response) {
        try{
            if(photoIds.length == 1) {
                File file = photoService.getImageFile(photoIds[0]);
                OutputStream outputStream = response.getOutputStream();
                IOUtils.copy(new FileInputStream(file), outputStream);
                outputStream.close();
            } else {
                // TODO: 여러장 다운로드 시 zip파일로 묶어서 다운로드
            }
        } catch(FileNotFoundException e) {
            throw new RuntimeException("Error");
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

}
