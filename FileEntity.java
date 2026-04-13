package com.example.Filesharingapp.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String fileName;

    private String uploadedBy;   // ✅ FIXED

    private LocalDateTime uploadTime;   // ✅ FIXED

    private LocalDateTime expiryTime;   // ✅ FIXED

    @Lob
    @Column(name = "file_data", columnDefinition = "LONGBLOB")
    private byte[] fileData;   // ✅ FIXED
}