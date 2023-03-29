package com.quanht.service;

import com.quanht.entities.Image;
import com.quanht.exception.BadRequestException;
import com.quanht.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService {

    private ImageRepository imageRepository;

    private Path rootPath = Paths.get("images");

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
        createFolder(rootPath.toString());
    }

    private void createFolder(String path){
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    public Image saveToDB(Image image){
        return imageRepository.save(image);
    }

    public String uploadFile(MultipartFile file) {

        validateFile(file);

        String fileName = file.getOriginalFilename();
        // Tao fileId
        String fileId = String.valueOf(System.currentTimeMillis());


        try {
            String url = rootPath + "/" + fileId + getExtensionFile(fileName);
            File serverFile = new File(url);
            // Sử dụng Buffer để lưu dữ liệu từ file
            OutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));

            stream.write(file.getBytes());
            stream.close();

            return url;
        } catch (Exception e){
            throw new BadRequestException("Không thể tải file");
        }
    }

    public void validateFile(MultipartFile file){
        // Kiem tra ten
        String fileName = file.getOriginalFilename();
        if(fileName == null || file.equals("")){
            throw new BadRequestException("Tên file không được trống");
        }

        // Kiem tra extension
        // avatar.png -> png
        // avatar.jpg -> jpg
        String fileExtension = getExtensionFile(fileName);
        if(!checkFileExtension(fileExtension)){
            throw new BadRequestException("File không hợp lệ");
        }

        // Kiem tra size
        // Upload <= 3MB
        double size = file.getSize()/Math.pow(1024,2);
        if(size > 3){
            throw new BadRequestException("Kích thước file không được vượt quá 3MB");
        }
    }

    public String getExtensionFile(String fileName){
        int lastIndex = fileName.lastIndexOf(".");
        if(lastIndex == -1){
            return "";
        }
        return fileName.substring(lastIndex);
    }

    public boolean checkFileExtension(String fileExtension){
        List<String> extensions = new ArrayList<>(List.of(".png", ".jpeg", ".jpg"));
        return extensions.contains(fileExtension);
    }
}
