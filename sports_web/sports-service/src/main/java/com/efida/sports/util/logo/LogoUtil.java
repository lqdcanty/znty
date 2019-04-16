package com.efida.sports.util.logo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyan on 2018/8/14.
 */
public class LogoUtil {

    public static List<Logo> getCompanyList() {
        String url="http://static.wx.zntyydh.com/logo/";
        List<Logo> list= new ArrayList<Logo>();
        list.add(getLogo("国家体育总局","http://www.sport.gov.cn",url+"logo_gjtyj@2x.png"));
        list.add(getLogo("浙江省政府","http://www.zj.gov.cn",url+"logo_zjrm@2x.png"));
        list.add(getLogo("浙江省体育局","http://www.zjsports.gov.cn",url+"logo_zjstyj@2x.png"));
        list.add(getLogo("杭州余杭区政府","http://www.yuhang.gov.cn",url+"logo_hzyh@2x.png"));
        list.add(getLogo("中华全国体育总会","http://www.sport.org.cn",url+"logo_zhqg@2x.png"));
        list.add(getLogo("华奥星空","http://3g.sports.cn/index.php",url+"logo_haxk@2x.png"));
        list.add(getLogo("华运智体","http://huasports.com",url+"logo_hyzt@2x.png"));
        list.add(getLogo("浙数文化","http://www.600633.cn/zbcm/index.shtml",url+"logo_zswh@2x.png"));
        list.add(getLogo("黑岩文化","http://www.sh-blackrock.com",url+"logo_yd@2x.png"));
        list.add(getLogo("天翼云","http://m.ctyun.cn",url+"logo_tyyun@2x.png"));
        list.add(getLogo("安妮股份","http://www.anne.com.cn",url+"logo_angf@2x.png"));
        return list;
    }

    public static Logo getLogo(String name,String url,String logoUrl){
        Logo logo=new Logo();
        logo.setLogo(logoUrl);
        logo.setUrl(url);
        logo.setName(name);
        return logo;
    }

}
