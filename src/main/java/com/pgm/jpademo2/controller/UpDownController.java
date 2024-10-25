package com.pgm.jpademo2.controller;

import com.pgm.jpademo2.dto.BoardDTO;
import com.pgm.jpademo2.dto.upload.UploadFileDTO;
import com.pgm.jpademo2.dto.upload.UploadResultDTO;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@Log4j2
@RequestMapping("/upload")
public class UpDownController {

    @Value("${com.pgm.jpademo2.upload.path}")
    private String uploadPath;

    @GetMapping("/uploadForm")
    public void uploadForm() {
    }

    @PostMapping(value = "/uploadPro", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadPro(UploadFileDTO uploadFileDTO, BoardDTO boardDTO, Model model) {
        //log.info(uploadFileDTO);
        log.info(boardDTO);
        if (uploadFileDTO.getFiles() != null) {
            final List<UploadResultDTO> list = new ArrayList<>();
            uploadFileDTO.getFiles().forEach(multipartFile -> {
                String originalName = multipartFile.getOriginalFilename();
                log.info(originalName);
                String uuid = UUID.randomUUID().toString();
                Path savePath = Paths.get(uploadPath, uuid + "_" + originalName);
                boolean image = false;
                try {
                    //실제 파일을 저장하는 명령어
                    multipartFile.transferTo(savePath);
                    //이미지 파일의 종류라면
                    if (Files.probeContentType(savePath).startsWith("image")) {
                        image = true;
                        File thumbFile = new File(uploadPath, "s_" + uuid + "_" + originalName);
                        Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 100, 100);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                list.add(UploadResultDTO.builder()
                        .uuid(uuid)
                        .image(image)
                        .fileName(originalName)
                        .build());
                model.addAttribute("list", list);
                model.addAttribute("uploadPath", uploadPath);
            });
        }

    }
    @GetMapping("/view/{fileName}")

    @ResponseBody
    public ResponseEntity<Resource> viewFileGet(@PathVariable("fileName") String fileName) {
        Resource resource = new FileSystemResource(uploadPath +File.separator + fileName);
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();

        try {
            headers.add("Content-Type", Files.probeContentType( resource.getFile().toPath() ));
        }catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }
    //삭제 구현
    @GetMapping("/remove")
    public String removeFile(@RequestParam("fileName") String fileName){
        log.info("AAAAA"+fileName);

        Resource resource = new FileSystemResource(uploadPath+File.separator + fileName);
        String resourceName = resource.getFilename();

        Map<String, Boolean> resultMap = new HashMap<>();
        boolean removed = false;

        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();

            //섬네일이 존재한다면
            if(contentType.startsWith("image")){
                String fileName1=fileName.replace("s_","");
                File originalFile = new File(uploadPath+File.separator + fileName1);
                originalFile.delete();
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        resultMap.put("result", removed);
        return "/upload/uploadForm";
    }


}





