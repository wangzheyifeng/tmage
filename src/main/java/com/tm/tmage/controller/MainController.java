package com.tm.tmage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

@Controller
public class MainController {

    @RequestMapping({"/", "/index", "default"})
    public String getIndex() {
        return "pages/index";
    }

    @RequestMapping("/uploadFile")
    @ResponseBody
    public Map<String, String> getFile(@RequestParam(value = "file") MultipartFile file) {
        Map<String, String> result = new HashMap<>();
        try {
            String p = System.getProperty("user.dir"); //获取项目路径
            System.out.println("项目路径：" + p);
            Path superPath = Paths.get(p).getParent(); //获取项目路径的上级目录
            System.out.println("项目路径的上级路径：" + superPath.toString());
            //java只能一次创建一级目录
            Path imageDir = Paths.get(p, "image");
            System.out.println("image路径：" + imageDir);
            //判断是否有image文件夹,没有就创建
            File f = new File(imageDir.toString());
            if (!f.exists()) {
                f.mkdir();
            }

            ResourceBundle resource = null;
            resource = ResourceBundle.getBundle("application");
            String backupPath = resource.getString("imagepath");
            //生成文件名
            String filename = UUID.randomUUID().toString().toUpperCase().replace("-", "") + ".jpg";

            Path imagePath = Paths.get(imageDir.toString(), filename);
            if (null != backupPath && "" != backupPath) {
                //将文件路径替换为自定义的
                imagePath = Paths.get(backupPath, filename);
            }
            //替换路径08.18
            imagePath = Paths.get(backupPath, filename);
            //创建文件，并将传过来的图片复制进去
            File f1 = new File(imagePath.toString());
            if (!f1.exists()) {
                f1.createNewFile();
            }
            file.transferTo(f1);
            System.out.println("最终生成的文件路径：" + imagePath.toString());
            //ResourceBundle resource= null;

            // resource = ResourceBundle.getBundle("application");
            //  String url = resource.getString("url");
             //http://localhost:8080/
//            result.put("path", "http://47.98.234.89:8080" + "/image/" + filename);
            result.put("path", "http://localhost:8080" + "/image/" + filename);
        } catch (Exception e) {
            result.put("Exception", e.getMessage());
        }

        return result;
    }

}
