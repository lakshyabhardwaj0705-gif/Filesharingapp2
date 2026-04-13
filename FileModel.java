package com.example.Filesharingapp.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FileModel{
 private int id;
 private String fileName;
 private String uploadedby;
 public LocalDateTime uploadTime;
 public LocalDateTime expiryTime;
 private byte[] fileData;
}