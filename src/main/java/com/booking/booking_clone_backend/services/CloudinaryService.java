package com.booking.booking_clone_backend.services;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {

    UploadResult uploadImage(MultipartFile file, String folder, String name);

    public record UploadResult(String url, String publicId) {}
}
