package com.lwh.controller;

import com.lwh.util.OSSUtil;
import com.lwh.vo.ConstantProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@Controller
@Slf4j
public class UploadController {
    @Autowired
    private OSSUtil aliyunOSSUtil;
    @Autowired
    private ConstantProperties constantProperties;
    @GetMapping("/toUploadBlog")
    public String toUploadBlog(){
        return "oss/upload";
    }

    @PostMapping("/upload/toUploadBlog")
    public String toUploadBlogPost(MultipartFile file){
        log.info("=========>文件上传");
        try {

            if(null != file){
                String filename = file.getOriginalFilename();
                if(!"".equals(filename.trim())){
                    File newFile = new File(filename);
                    FileOutputStream os = new FileOutputStream(newFile);
                    os.write(file.getBytes());
                    os.close();
//                    file.transferTo(newFile);
                    //上传到OSS
                    String uploadUrl = aliyunOSSUtil.upload(newFile);
                    System.out.println("https://lwh1212.oss-cn-guangzhou.aliyuncs.com/"+uploadUrl);

                }

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "oss/index";
    }

    @GetMapping("/getObjectList")
    @ResponseBody
    public List<String> getObjectList(){
        String bucketName=constantProperties.getBucketname();
        System.out.println(bucketName);
        List<String> objectList = aliyunOSSUtil.getObjectList(bucketName);
        return objectList;
    }

    @GetMapping("/delete")
    @ResponseBody
    public String deleteBlog(@RequestParam("key")String key){
        aliyunOSSUtil.deleteBlog(key);
        return "删除成功";
    }
}
