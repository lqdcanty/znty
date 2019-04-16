package com.efida.sports.web.controller;

import java.util.HashMap;
import java.util.Map;

import com.efida.sports.service.dubbo.intergration.FileUploadFacadeClient;
import com.efida.sports.web.util.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.efida.file.service.FileUploadApiService;
import com.efida.file.vo.FileTransmissionVo;
import com.efida.sports.util.JsonResultUtil;

import io.swagger.annotations.Api;

/**
 * 文件上传
 * 
 * @author zengbo
 * @version $Id: FileUploadController.java, v 0.1 2018年5月29日 下午2:51:05 zengbo Exp $
 */
@Controller
@Api(value = "fileApi", tags = { "文件上传操作接口" })
@RequestMapping(value = "file", produces = "application/json;charset=UTF-8")
public class FileUploadController {

    private Logger               logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FileUploadFacadeClient fileUploadFacadeClient;

    @RequestMapping("upload")
    @ResponseBody
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile uploadfile) {
        try {
            FileTransmissionVo vo = new FileTransmissionVo();

            byte[] bytes = uploadfile.getBytes();
            String name = uploadfile.getOriginalFilename();
            vo.setBtyes(bytes);
            vo.setFileName(name);
            vo.setAppName("sport");
            vo.setLastingType("2");
            vo.setUiserId("sport_web");
            vo.setFileType(name.split("\\.")[1]);
            String url = fileUploadFacadeClient.uploadSourceFile(vo);
            JSONObject json = (JSONObject) JSONObject.parse(url);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("url", json.get("url"));
            return JsonResultUtil.getSuccessResult(map);
        } catch (Exception e) {
            logger.error("", e);
            return JsonResultUtil.getServerErrorResult("文件上传失败");
        }
    }

    @RequestMapping("upload/base64")
    @ResponseBody
    public Map<String, Object> uploadBase64(@RequestParam("file") String base64) {
        logger.info("**********************upload/base64****************************");
        logger.info("**********************upload/base64****************************    base64=【"+"】");
        logger.info("**********************upload/base64****************************    base64.length()===="+base64.length());
        try {
            if(StringUtils.isBlank(base64)){
                return JsonResultUtil.getServerErrorResult("文件上传失败,base64为空");
            }
            FileTransmissionVo vo = new FileTransmissionVo();
            MultipartFile uploadfile = FileUtils.base64ToMultipart(base64);
            if(uploadfile == null){
                return JsonResultUtil.getServerErrorResult("文件上传失败,base64长度为"+base64.length());
            }
            byte[] bytes = uploadfile.getBytes();
            String name = uploadfile.getOriginalFilename();
            vo.setBtyes(bytes);
            vo.setFileName(name);
            vo.setAppName("sport");
            vo.setLastingType("2");
            vo.setUiserId("sport_web");
            vo.setFileType(name.split("\\.")[1]);
            String url = fileUploadFacadeClient.uploadSourceFile(vo);
            JSONObject json = (JSONObject) JSONObject.parse(url);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("url", json.get("url"));
            return JsonResultUtil.getSuccessResult(map);
        } catch (Exception e) {
            logger.error("", e);
            return JsonResultUtil.getServerErrorResult("文件上传失败");
        }
    }

}
