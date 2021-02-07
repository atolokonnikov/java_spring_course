package org.example.app.services;

import org.apache.log4j.Logger;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@Service
public class FileService {

    private Logger logger = Logger.getLogger(FileService.class);
    public static ArrayList<File> fileList;
    private static final String rootPath = System.getProperty("catalina.home");

    public static String GetUploadDirectory() {
        String uploadPath = rootPath + File.separator + "external_uploads";
        return uploadPath;
    }

    public static String GetDownloadDirectory() {
        String downloadPath = rootPath + File.separator + "downloaded_files";
        return downloadPath;
    }

    public static MultipartFile GetSourceFileByName(String fileName){
        Path path = Paths.get(GetUploadDirectory() + File.separator + fileName);
        String contentType = "text/plain";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        }
        catch (final IOException e){}

        MultipartFile multipartFile = new MockMultipartFile(fileName, fileName, contentType, content);
        return multipartFile;
    }

    public static File TouchDir(String dirName) {
        File dir;
        dir = new File(dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static String TransferFile(MultipartFile sourceFile, String tergetDirecpory) throws Exception {
        String name = sourceFile.getOriginalFilename();
        byte[] bytes = sourceFile.getBytes();

        //create dir
        File dir = FileService.TouchDir(tergetDirecpory);

        // create file
        File targetFile = new File(dir.getAbsolutePath() + File.separator + name);
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(targetFile));
        stream.write(bytes);
        stream.close();
        return targetFile.getAbsolutePath();
    }

    public static void AddFileToFileList(String targetFileFillPath) {
        File newFile = new File(targetFileFillPath);
        for (File item : fileList) {
            if (item.getName().equals(newFile.getName())) {
                return;
            }
        }
        fileList.add(newFile);
    }

    public ArrayList getFileList(String directoryPath) {
        ArrayList<File> fileList = new ArrayList<File>();
        File dir = new File(directoryPath);
        if (dir.isDirectory()) {
            for (File item : dir.listFiles()) {
                logger.info("File name = " + item.getName());
                fileList.add(item);
            }
        }
        return fileList;
    }

    @PostConstruct
    public void defaultInit() {
        fileList = getFileList(GetUploadDirectory());
        logger.info("FileService INIT");
        logger.info("fileList.size() = " + fileList.size());
    }
}
