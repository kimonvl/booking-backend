package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.exceptions.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {

    UploadResult uploadImage(MultipartFile file, String folder, String name) throws FileUploadException;

    public record UploadResult(String url, String publicId) {}
}
