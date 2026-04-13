package com.example.Filesharingapp.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.Filesharingapp.service.FileService;

@Controller
@RequestMapping("/files")
public class Filecontroller {

    @Autowired
    private FileService fileService;

    // ✅ Handle the login/landing page at /files
    @GetMapping
    public String login() {
        return "home"; // renders templates/home.html
    }

    // ✅ Main page
    @GetMapping("/home")
    public String index(Model model) {
        model.addAttribute("files", fileService.getAll());
        return "list-files";   // templates/list-files.html
    }

    // ✅ Upload file
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                            @RequestParam("uploadedBy") String uploadedBy) throws IOException {

        fileService.uploadFile(file, uploadedBy);
        return "redirect:/files/home";
    }

    // ✅ Share file page
    @GetMapping("/share/{id}")
    public String shareFile(@PathVariable int id, Model model) {

        ResponseEntity<?> fileModel = fileService.shareFile(id);

        if (fileModel.hasBody()) {
            String currentUrl = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .toUriString();

            model.addAttribute("shareUrl", currentUrl);
            model.addAttribute("file", fileModel.getBody());

            return "share-file";   // templates/share-file.html
        } else {
            return "redirect:/files/home";
        }
    }

    // ✅ Delete file
    @PostMapping("/delete/{id}")
    public String deleteFile(@PathVariable int id) {

        fileService.deleteFile(id);
        return "redirect:/files/home";
    }

    // ✅ Download file
    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable int id) {
        return fileService.getFile(id);
    }

}