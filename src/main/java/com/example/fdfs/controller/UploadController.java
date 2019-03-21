package com.example.fdfs.controller;

import lombok.extern.slf4j.Slf4j;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import net.dongliu.apk.parser.bean.UseFeature;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lijz
 * @description: TODO
 * @date 2019/3/2011:00
 */
@RestController
@RequestMapping(value = "/upload")
@Slf4j
public class UploadController {
    @PostMapping(value = "/uploadApk")
    public Map<String,Object> uploadFile(@RequestParam("file") MultipartFile multipartFile){
        Map<String,Object> map = new HashMap<>();
        try {
//            CommonsMultipartFile commonsmultipartfile = (CommonsMultipartFile) multipartFile;
            String originalFilename = multipartFile.getOriginalFilename();
            String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));//截取文件后缀
            // 2.1、获取文件后缀名
//            String extension = StringUtils.substringAfterLast(multipartFile.getOriginalFilename(), ".");
            System.out.println("上传文件名为："+originalFilename);
            File tempFile = File.createTempFile("temp", fileSuffix);//创建临时文件
            System.out.println("临时文件所在的本地路径：" + tempFile.getCanonicalPath());
            multipartFile.transferTo(tempFile);
            ApkFile apkFile = new ApkFile(tempFile);
            ApkMeta apkMeta = apkFile.getApkMeta();
            System.out.println(apkMeta.getLabel());
            System.out.println(apkMeta.getPackageName());
            System.out.println(apkMeta.getVersionCode());
            System.out.println(apkMeta.getVersionName());
            for (UseFeature feature : apkMeta.getUsesFeatures()) {
                System.out.println(feature.getName());
            }
            map.put("code",200);
            map.put("message","上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code",500);
            map.put("message","上传失败！"+e);
        }finally {
            map.put("timestamp",System.currentTimeMillis());
        }
        return map;
    }

    public void createTempFile() throws Exception {
        final File htmlFile = File.createTempFile("temp", ".html");//创建临时文件
        log.info("临时文件所在的本地路径：" + htmlFile.getCanonicalPath());
        FileOutputStream fos = new FileOutputStream(htmlFile);
        try {
            //这里处理业务逻辑
        } finally {
            //关闭临时文件
            fos.flush();
            fos.close();

//            htmlFile.deleteOnExit();//程序退出时删除临时文件
            htmlFile.delete();//立即删除临时文件
        }
    }
}
