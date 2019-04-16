package com.efida.sports.service.dubbo.intergration;

import java.util.Date;
import java.util.List;

import com.efida.sport.admin.facade.model.SpMatchAllInfoModel;
import com.efida.sport.admin.facade.model.SpMatchGroupAndItemInfoModel;
import com.efida.sport.admin.facade.model.SpMatchGroupModel;
import com.efida.sport.admin.facade.model.SpMatchInfoModel;
import com.efida.sport.admin.facade.model.SpMatchModel;
import com.efida.sport.admin.facade.model.SpPlayingAreaModel;
import com.efida.sport.admin.facade.model.open.MatchCollectionModel;
import com.efida.sport.admin.facade.model.open.MatchDetailModel;
import com.efida.sport.admin.facade.model.open.MatchGroupItemModel;
import com.efida.sport.admin.facade.model.page.SportsAdminPageResult;

public interface SpMatchFacadeClient {
    /**
     * 获取赛事列表
     * @param gameCode   项目分类Code
     * @return
     */
    List<SpMatchModel> getMatchs(String gameCode);

    /**
     * 查询比赛组和比赛项数据 
     * 
     * @param playingAreaId  赛场Id
     * @return
     */
    List<SpMatchGroupAndItemInfoModel> getMatchTypeAndGroupInfo(String fileidCode);

    /**
     * 查询赛场列表数据
     * 
     * @param matchCode 赛事code
     * @return
     */
    List<SpPlayingAreaModel> getSpPlayingAreas(String matchCode);

    /**
     * 获取赛事详情
     * @param code
     * @return 
     */
    SpMatchInfoModel getMatchDetail(String code);

    /**
     * 获取比赛项详细信息
     * 
     * @param itemId
     * @return
     */
    SpMatchAllInfoModel getItemDetail(String fileidCode, String itemCode);

    /**
     * 获取赛事信息
     * 
     * @param matchCode
     * @param matchStatus
     * @param beginTimeMin
     * @param beginTimeMax
     * @param endTimeMin
     * @param endTimeMax
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public List<MatchDetailModel> queryMatches(String matchCode, String matchStatus,
                                               Date beginTimeMin, Date beginTimeMax,
                                               Date endTimeMin, Date endTimeMax, int pageNumber,
                                               int pageSize);

    /**
     * 根据项目编号获取赛事列表
     * 
     * @param gameCode
     * @param pageNumber
     * @param pageSize
     * @return
     */
    SportsAdminPageResult<SpMatchModel> getPageMatchs(String gameCode, int pageNumber,
                                                      int pageSize);

    /**
     * 
     * 搜索赛事列表
     * @param keyword
     * @param pageNumber
     * @param pageSize
     * @return
     */
    SportsAdminPageResult<SpMatchModel> searchMatchs(String keyword, int pageNumber, int pageSize);

    /**
     * 获取赛事信息
     * 
     * @param matchCode
     * @return
     */
    SpMatchModel getEnableSpMatch(String matchCode);

    /**
     * 获取赛场-分组-项
     *
     * @param matchCode
     * @return
     */
    public List<MatchGroupItemModel> getMatchGroupItemList(String matchCode);

    /**
    * 获取比赛组信息
    * 
    * @param areaGroupCode
    * @return
    */
    SpMatchGroupModel getSpMatchGroupModel(String areaGroupCode);

    /**
     * 赛事集查询
     * 
     * @param matchCode
     * @return
     */
    List<MatchCollectionModel> getSpMatchCollections(String matchCode);

    /**
     * 根据matchCode查询分赛场
     * 
     * @param matchCode
     * @return
     */
    public List<SpPlayingAreaModel> getPlayingAreas(String matchCode);

    /**
     * 查询比赛地址
     * 
     * @param siteCode
     * @return
     */
    String getFieldAddress(String siteCode);

}
