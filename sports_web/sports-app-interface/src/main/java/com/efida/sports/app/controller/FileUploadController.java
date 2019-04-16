package com.efida.sports.app.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.efida.file.service.FileUploadApiService;
import com.efida.file.vo.FileTransmissionVo;
import com.efida.sports.util.JsonResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 文件上传
 * 
 * @author zengbo
 * @version $Id: FileUploadController.java, v 0.1 2018年5月29日 下午2:51:05 zengbo Exp $
 */
@Controller
@Api(value = "fileApi", tags = { "文件上传操作接口" })
@RequestMapping(value = "/api/file", produces = "application/json;charset=UTF-8")
public class FileUploadController {

    private Logger               logger = LoggerFactory.getLogger(this.getClass());

    @Reference
    private FileUploadApiService fileUploadApiService;

    @RequestMapping(value = "upload", method = { RequestMethod.POST })
    @ApiOperation(value = "文件上传", notes = "文件上传")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ResponseBody
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile uploadfile) {
        try {
            FileTransmissionVo vo = new FileTransmissionVo();

            byte[] bytes = uploadfile.getBytes();
            String name = uploadfile.getOriginalFilename();
            vo.setBtyes(bytes);
            logger.info(""+vo.getBtyes());
            vo.setFileName(name);
            logger.info(""+vo.getFileName());
            vo.setAppName("sport");
            logger.info(""+vo.getAppName());
            vo.setLastingType("2");
            logger.info(""+vo.getLastingType());
            vo.setUiserId("sport_app");
            logger.info(""+vo.getUiserId());
            vo.setFileType(name.split("\\.")[1]);
            logger.info(""+vo.getFileType());
            String url = fileUploadApiService.uploadSourceFile(vo);
            JSONObject json = (JSONObject) JSONObject.parse(url);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("url", json.get("url"));
            logger.info("文件上传成功地址:" + url);
            return JsonResultUtil.getSuccessResult(map);
        } catch (Exception e) {
            logger.error("文件上传失败:{0}", e);
            return JsonResultUtil.getServerErrorResult("文件上传失败");
        }
    }
}
