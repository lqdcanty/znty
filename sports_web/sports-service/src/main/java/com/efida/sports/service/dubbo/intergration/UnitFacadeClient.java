package com.efida.sports.service.dubbo.intergration;

import com.efida.sport.dmp.facade.model.UnitAccountDto;
import com.efida.sport.dmp.facade.result.DmpPageResult;

public interface UnitFacadeClient {

    DmpPageResult<UnitAccountDto> getList(String keyword, int currentPage, int pageSzie);

}
