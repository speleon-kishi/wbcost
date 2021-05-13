package com.tjrac.wbcost.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class XmlToDocx {
    /**
     * @param documentFile 动态生成数据的xml文件
     * @param toFilePath  需要导出的文件名及路径
     * @throws ZipException
     * @throws IOException
     */
    public void outDocx(File documentFile, String toFilePath) throws ZipException, IOException {
        try {
            ClassPathResource resource = new ClassPathResource("static/templates.docx");
            InputStream input = resource.getInputStream();
            File temp = File.createTempFile("templates-" + UUID.randomUUID().toString().replaceAll("-", ""), ".docx");
            temp.deleteOnExit();
            int index;
            byte[] bytes = new byte[1024];
            FileOutputStream downloadFile = new FileOutputStream(temp.getAbsolutePath());
            while ((index = input.read(bytes)) != -1) {
                downloadFile.write(bytes, 0, index);
                downloadFile.flush();
            }
            input.close();
            downloadFile.close();
            ZipFile zipFile = new ZipFile(temp);
            Enumeration<? extends ZipEntry> zipEntrys = zipFile.entries();
            ZipOutputStream zipout = new ZipOutputStream(new FileOutputStream(toFilePath));
            int len = -1;
            byte[] buffer = new byte[1024];
            while (zipEntrys.hasMoreElements()) {
                ZipEntry next = zipEntrys.nextElement();
                InputStream is = zipFile.getInputStream(next);
                zipout.putNextEntry(new ZipEntry(next.toString()));
                if ("word/document.xml".equals(next.toString())) {
                    InputStream in = new FileInputStream(documentFile);
                    while ((len = in.read(buffer)) != -1) {
                        zipout.write(buffer, 0, len);
                    }
                    in.close();
                } else {
                    while ((len = is.read(buffer)) != -1) {
                        zipout.write(buffer, 0, len);
                    }
                    is.close();
                }
            }
            zipout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}