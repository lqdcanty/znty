/**
 * 
 */
package com.efida.sport.facade.service;

import com.efida.sport.facade.model.ApplyInfoModel;
import com.efida.sport.facade.model.DefaultResult;

/**
 * @author Administrator
 *  报名信息接口
 */
public interface ApplyInfoFacadeService {
	
	public DefaultResult<Boolean>  syncApplyInfo(ApplyInfoModel applyInfoModel);
	
	

}
