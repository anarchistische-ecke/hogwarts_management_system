package pro.sky.hogwarts_management_system.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.hogwarts_management_system.model.Avatar;
import pro.sky.hogwarts_management_system.model.Student;
import pro.sky.hogwarts_management_system.repository.AvatarRepository;
import pro.sky.hogwarts_management_system.repository.StudentRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Slf4j
@Service
public class AvatarService {

    @Autowired
    public AvatarService(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

    @Autowired
    StudentRepository studentRepository;

    private final AvatarRepository avatarRepository;

    public Optional<Avatar> findByStudentId(Long studentId) {
        return avatarRepository.findByStudentId(studentId);
    }

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        log.info("Avatars directory: {}", avatarsDir);
        log.info("Full path: {}", Paths.get(avatarsDir).toAbsolutePath());
        Student student = studentRepository.getById(studentId);
        String extension = getExtension(avatarFile.getOriginalFilename());
        Path filePath = Path.of(avatarsDir, studentId + "." + extension);
        try {
            Files.createDirectories(filePath.getParent());
        } catch (IOException e) {
            throw new IOException("Failed to create directory: " + filePath.getParent(), e);
        }
        Files.deleteIfExists(filePath);
        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = findAvatar(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());
        avatarRepository.save(avatar);
    }

    public Avatar findAvatar(Long studentId) {
        return avatarRepository.findByStudentId(studentId)
                .orElse(new Avatar()); // Return existing or new Avatar
    }

    private String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    public Page<Avatar> getAllAvatars(Pageable pageable) {
        return avatarRepository.findAll(pageable);
    }
}
