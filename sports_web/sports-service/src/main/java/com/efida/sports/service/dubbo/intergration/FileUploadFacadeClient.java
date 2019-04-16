package com.efida.sports.service.dubbo.intergration;


import com.efida.file.vo.FileTransmissionVo;

/**
 * Created by wangyan on 2018/9/28.
 */
public interface FileUploadFacadeClient {

    String uploadSourceFile(FileTransmissionVo vo);

}
