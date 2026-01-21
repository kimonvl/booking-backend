package com.booking.booking_clone_backend.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService{
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public UploadResult uploadImage(MultipartFile file, String folder, String name) {
        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folder,
                            "public_id", name,
                            "resource_type", "image"
                    )
            );

            String secureUrl = (String) result.get("secure_url");
            String publicId = (String) result.get("public_id");

            return new UploadResult(secureUrl, publicId);
        } catch (IOException e) {
            throw new RuntimeException("Cloudinary upload failed for: " + file.getOriginalFilename(), e);
        }
    }
}
