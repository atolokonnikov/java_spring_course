package org.example.app.services;

import java.io.File;

public class FileService {

    public static File TouchDir(String dirName) {
        File dir;
        String rootPath = System.getProperty("catalina.home");
        dir = new File(rootPath + File.separator + dirName);
        if (!dir.exists()){
            dir.mkdirs();
        }
        return dir;
    }
}
