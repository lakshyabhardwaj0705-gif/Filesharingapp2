package com.example.Filesharingapp.service;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.example.Filesharingapp.model.FileModel;

public interface FileService {
public List<FileModel> getAll();
public ResponseEntity<?> uploadFile(MultipartFile file, String uploadedBy) throws IOException;
public ResponseEntity<?> shareFile(int id);
public ResponseEntity<?> deleteFile(int id);
public ResponseEntity<?> getFile(int id);
public void deleteExpiredFiles();
}