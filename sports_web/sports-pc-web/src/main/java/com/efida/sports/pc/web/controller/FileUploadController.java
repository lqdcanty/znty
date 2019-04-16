package com.efida.sports.pc.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
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

    @Reference
    private FileUploadApiService fileUploadApiService;

    @RequestMapping("pc/upload")
    @ResponseBody
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile uploadfile) {
        try {
            logger.info("开始文件上传");
            FileTransmissionVo vo = new FileTransmissionVo();

            byte[] bytes = uploadfile.getBytes();
            String name = uploadfile.getOriginalFilename();
            vo.setBtyes(bytes);
            vo.setFileName(name);
            vo.setAppName("sport");
            vo.setLastingType("2");
            vo.setUiserId("sport_pc_web");
            vo.setFileType(name.split("\\.")[1]);
            logger.info("开始文件上参数", JSON.toJSONString(vo));
            String url = fileUploadApiService.uploadSourceFile(vo);
            JSONObject json = (JSONObject) JSONObject.parse(url);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("url", json.get("url"));
            logger.info("文件上传成功地址:" + url);
            return JsonResultUtil.getSuccessResult(map);
        } catch (Exception e) {
            logger.error("文件上传失败", e);
            return JsonResultUtil.getServerErrorResult("文件上传失败");
        }
    }
}
