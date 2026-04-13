package com.example.Filesharingapp.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.Filesharingapp.entity.FileEntity;
import com.example.Filesharingapp.model.FileModel;
import com.example.Filesharingapp.repository.FileRepository;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository fileRepository;

    // ✅ Convert Entity → Model
    private FileModel convertToModel(FileEntity entity) {
        FileModel model = new FileModel();
        BeanUtils.copyProperties(entity, model);
        return model;
    }

    // ✅ Get All Files
    @Override
    public List<FileModel> getAll() {
        return fileRepository.findAll()
                .stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    // ✅ Upload File
    @Override
    public ResponseEntity<FileModel> uploadFile(MultipartFile file, String uploadedBy) throws IOException {

        FileEntity entity = new FileEntity();

        entity.setFileName(file.getOriginalFilename());
        entity.setUploadedBy(uploadedBy);
        entity.setUploadTime(LocalDateTime.now());
        entity.setExpiryTime(LocalDateTime.now().plusDays(1));
        entity.setFileData(file.getBytes());

        fileRepository.save(entity);

        return new ResponseEntity<>(convertToModel(entity), HttpStatus.OK);
    }

    // ✅ Share File (Metadata)
    @Override
    public ResponseEntity<FileModel> shareFile(int id) {

        FileEntity entity = fileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));

        return new ResponseEntity<>(convertToModel(entity), HttpStatus.OK);
    }

    // ✅ Delete File
    @Override
    public ResponseEntity<String> deleteFile(int id) {

        FileEntity entity = fileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));

        fileRepository.delete(entity);

        return new ResponseEntity<>("File deleted successfully", HttpStatus.OK);
    }

    // ✅ Download File
    @Override
    public ResponseEntity<byte[]> getFile(int id) {

        FileEntity entity = fileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException( HttpStatus.NOT_FOUND,"File not found"));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + entity.getFileName() + "\"")
                .body(entity.getFileData());
    }

    // ✅ Auto Delete Expired Files
    @Override
@Scheduled(cron = "0 0 * * * *")
public void deleteExpiredFiles() {

    List<FileEntity> expiredFiles =
            fileRepository.findByExpiryTimeBefore(LocalDateTime.now());

    if (expiredFiles != null && !expiredFiles.isEmpty()) {
        fileRepository.deleteAll(expiredFiles);
        System.out.println("Expired files deleted at: " + LocalDateTime.now());
    }
}
}