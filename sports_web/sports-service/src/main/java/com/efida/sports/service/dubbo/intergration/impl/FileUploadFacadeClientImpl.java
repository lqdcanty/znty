package com.efida.sports.service.dubbo.intergration.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.efida.file.service.FileUploadApiService;
import com.efida.file.vo.FileTransmissionVo;
import com.efida.sports.service.dubbo.intergration.FileUploadFacadeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by wangyan on 2018/9/28.
 */
@Service("fileUploadFacadeClient")
public class FileUploadFacadeClientImpl implements FileUploadFacadeClient {

    @Reference
    private FileUploadApiService fileUploadApiService;

    private static Logger logger = LoggerFactory.getLogger(FileUploadFacadeClientImpl.class);


    @Override
    public String uploadSourceFile(FileTransmissionVo vo) {
        return fileUploadApiService.uploadSourceFile(vo);
    }
}
