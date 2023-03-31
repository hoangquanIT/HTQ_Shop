package com.quanht.service;

import com.quanht.entities.Image;
import com.quanht.entities.Product;
import com.quanht.exception.BadRequestException;
import com.quanht.exception.NotFoundException;
import com.quanht.repositories.ImageRepository;
import com.quanht.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ImageService {

    private ImageRepository imageRepository;

    private ProductRepository productRepository;

    private Path rootPath = Paths.get("src/main/resources/static/images");

    @Autowired
    public ImageService(ImageRepository imageRepository, ProductRepository productRepository) {
        this.imageRepository = imageRepository;
        this.productRepository = productRepository;
        createFolder(rootPath.toString());
    }

    private void createFolder(String path){
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    @Transactional
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

            return "/images/" + fileId + getExtensionFile(fileName);
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

    @Transactional
    public List<Image> uploadImagesForProduct(Long id, MultipartFile[] files){
        Product product = productRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Không tồn tại sản phẩm có id = " + id);
        });
        List<Image> list = new ArrayList<>();
        if (files.length > 0) {
            Arrays.stream(files).forEach(img -> {
                String pathFile = uploadFile(img);
                Image image = Image.builder()
                        .url(pathFile)
                        .product(product)
                        .build();
                imageRepository.save(image);
                list.add(image);
            });
        }
        return list;
    }

    @Transactional
    public void deleteImage(Long id){
        Image image = imageRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Không tìm thấy ảnh tương ứng");
        });

        String url = image.getUrl();
        Path file = Paths.get(rootPath + url);

        try {
            file.toFile().delete();
            imageRepository.delete(image);
        } catch (Exception e) {
            throw new BadRequestException("Đã có lỗi xảy ra");
        }
    }
}
