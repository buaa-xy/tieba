package com.buaa.markpp.controller;

import com.buaa.markpp.model.ExceptionType;
import com.buaa.markpp.model.MarkppException;
import com.buaa.markpp.model.Result;
import com.buaa.markpp.service.ImageService;
import com.buaa.markpp.util.FileHelper;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Pattern;

import java.io.*;

import static org.apache.commons.text.CharacterPredicates.*;

@RestController
@RequestMapping(path = "/img")
@Validated
public class ImageController {

    private static final RandomStringGenerator generator = new RandomStringGenerator.Builder()
            .withinRange('0', 'z')
            .filteredBy(DIGITS, ASCII_LOWERCASE_LETTERS)
            .build();

    @Value("${resource.storage-directory}")
    private String directory;

    @Value("${resource.allowed-suffixes}")
    private String[] suffixes;

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < suffixes.length; i++) {
            suffixes[i] = suffixes[i].toLowerCase();
        }
    }

    @PostMapping("/upload")
    public Result<String> upload(
            @RequestParam(name = "token", required = false) @Pattern(regexp = "^[\\da-z]{32}$") String token,
            @RequestParam(name = "image") MultipartFile multipartFile) throws IOException {
        Result<String> result = new Result<>();
        if (token == null) {
            token = generator.generate(32);
        }
        else if (!imageService.existsToken(token)) {
            throw new MarkppException(ExceptionType.NOT_FOUND);
        }
        File folder = new File(directory, token).getAbsoluteFile();
        if (folder.exists()) {
            FileHelper.removeFolder(folder);
        }
        if (!folder.mkdirs()) {
            throw new IOException("Cannot create folder");
        }
        String filename = multipartFile.getOriginalFilename();
        validateSuffix(filename);
        File file = new File(folder, filename);
        multipartFile.transferTo(file);
        imageService.addToken(token);
        return result.withData(token);
    }

    @GetMapping("/{token}")
    public void download(
            @PathVariable(name = "token") @Pattern(regexp = "^[\\da-z]{32}$") String token,
            HttpServletResponse response) throws IOException {
        File folder = new File(directory, token).getAbsoluteFile();
        if (!folder.exists() || !folder.isDirectory()) {
            throw new MarkppException(ExceptionType.NOT_FOUND);
        }
        File[] files = folder.listFiles();
        if (files == null) {
            throw new IOException("unexpected null old file list");
        }
        if (files.length == 0) {
            throw new IOException("folder is empty");
        }
        File file = files[0];
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            OutputStream os = response.getOutputStream();
            String fileName = file.getName();
            response.setContentType(FileHelper.getImageContentType(fileName));
            byte[] buffer = new byte[1024];
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
            os.flush();
            os.close();
        }
    }

    @DeleteMapping("/remove/{token}")
    public Result<Void> remove(
            @PathVariable(name = "token") @Pattern(regexp = "^[\\da-z]{32}$") String token)
            throws IOException {
        if (!imageService.existsToken(token)) {
            throw new MarkppException(ExceptionType.NOT_FOUND);
        }
        FileHelper.removeFolder(new File(directory, token).getAbsoluteFile());
        imageService.removeToken(token);
        return new Result<>();
    }

    private void validateSuffix(String filename) {
        if (filename == null) {
            throw new MarkppException("文件名为空");
        }
        String suffix = FileHelper.getExtension(filename).toLowerCase();
        for (String s : suffixes) {
            if (s.equals(suffix))
                return;
        }
        throw new MarkppException("不支持的文件格式");
    }
}
