package moodtracker.controller;

import moodtracker.entity.User;
import moodtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/certificate")
    public ResponseEntity<String> uploadCertificate(
             @RequestParam("file") MultipartFile file,
            @RequestParam("email") String email) throws IOException{

       User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found"));

        // Δημιουργία φακέλου αν δεν υπάρχει
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Αποθήκευση αρχείου με unique όνομα
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Αποθήκευση path στον χρήστη
        user.setCertificatePath(filename);
        userRepository.save(user);

        return ResponseEntity.ok(filename);
    }

    @GetMapping("/certificate/{filename}")
    public ResponseEntity<Resource> getCertificate(
            @PathVariable String filename) throws MalformedURLException {

        Path filePath = Paths.get(uploadDir).resolve(filename);
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, 
                    "inline; filename=\"" + filename + "\"")
            .contentType(MediaType.APPLICATION_PDF)
            .body(resource);
    }
}