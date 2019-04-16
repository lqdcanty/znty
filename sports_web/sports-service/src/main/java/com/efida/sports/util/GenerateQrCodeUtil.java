/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.efida.sports.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * 
 * @author zoutao
 * 生成二维码帮助类
 * @version $Id: GenerateQrCodeUtil.java, v 0.1 2016年11月11日 上午10:01:02 zoutao Exp $
 */
public class GenerateQrCodeUtil {
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    /**
    * 生成二维码图片 不存储 直接以流的形式输出到页面
    * @param content
    * @param response
    */
    public static void encodeQrcode(String content, HttpServletResponse response) {
        if (StringUtils.isBlank(content))
            return;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Map<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); //设置字符集编码类型
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 300, 300, hints);
            BufferedImage image = toBufferedImage(bitMatrix);
            //输出二维码图片流
            try {
                ImageIO.write(image, "png", response.getOutputStream());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (WriterException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}