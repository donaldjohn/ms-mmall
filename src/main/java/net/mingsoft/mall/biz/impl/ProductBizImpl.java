package net.mingsoft.mall.biz.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.mingsoft.base.dao.IBaseDao;
import com.mingsoft.basic.biz.IBasicCategoryBiz;
import com.mingsoft.basic.biz.IBasicTypeBiz;
import com.mingsoft.basic.biz.ICategoryBiz;
import com.mingsoft.basic.biz.IModelBiz;
import com.mingsoft.basic.biz.impl.BasicBizImpl;
import com.mingsoft.basic.entity.BasicCategoryEntity;
import com.mingsoft.basic.entity.CategoryEntity;
import com.mingsoft.basic.entity.ModelEntity;
import com.mingsoft.basic.biz.IColumnBiz;
import com.mingsoft.mdiy.biz.IContentModelBiz;
import com.mingsoft.basic.entity.ColumnEntity;
import com.mingsoft.mdiy.entity.ContentModelEntity;
import com.mingsoft.util.PageUtil;
import com.mingsoft.util.StringUtil;

import net.mingsoft.mall.biz.IProductBiz;
import net.mingsoft.mall.constant.e.ProductEnum;
import net.mingsoft.mall.dao.IProductDao;
import net.mingsoft.mall.entity.ProductEntity;

/**
 * 
 * 
 * <p>
 * <b>铭飞MS平台</b>
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014 - 2015
 * </p>
 * 
 * <p>
 * Company:景德镇铭飞科技有限公司
 * </p>
 * 
 * @author 姓名 史爱华
 * 
 * @version 300-001-001
 * 
 *          <p>
 *          版权所有 铭飞科技
 *          </p>
 * 
 *          <p>
 *          Comments:商品管理业务处理层实现类 || 继承IBasicBiz业务处理层||实现IProductBiz业务层接口
 *          </p>
 * 
 *          <p>
 *          Create Date:2014-7-14
 *          </p>
 * 
 *          <p>
 *          Modification history:
 *          </p>
 */
@Service("ProductBizImpl")
public class ProductBizImpl extends BasicBizImpl implements IProductBiz {
	
	/**
	 * 产品dao处理层
	 */
	@Autowired
	private IProductDao productDao;
	
	
	/**
	 * 
	 */
	@Autowired
	private ICategoryBiz categoryBiz;
	
	
	/**
	 * 
	 */
	@Autowired
	private IColumnBiz columnBiz;
	
	/**
	 * 
	 */
	@Autowired
	private IContentModelBiz contentModelBiz;
	
	/**
	 * 模块管理业务层
	 */
	@Autowired
	private IModelBiz modelBiz;
	
	/**
	 * 
	 */
	@Autowired
	private IBasicCategoryBiz basicCategoryBiz;
	
	
	/**
	 * 根据appID查询产品的总数
	 * @param appId
	 * @return
	 */
	@Override
	public int getCountByAppId(Integer appId) {
		// TODO Auto-generated method stub
		return productDao.count(appId);
	}
	
	/**
	 * 根据appid查询产品分页
	 * @param appId 
	 * @param page：页面信息
	 * @param orderBy：排序方式
	 * @param order：是否采用升序
	 * @return
	 */
	@Override
	public List<ProductEntity> queryListPageByAppId(Integer appId, PageUtil page) {
		return productDao.queryPageByAppId(appId, page.getPageNo(), page.getPageSize());
	}
	
	
	/**
	 * 根据分类与时间查询商品列表
	 * @param categoryId :分类id
	 * @param dateTime ：更新时间
	 * @param appId ：应用appID
	 * @return
	 */
	@Override
	public List<ProductEntity> query(Integer categoryId, String dateTime,
			Integer appId) {
		// TODO Auto-generated method stub
		return productDao.queryListByTime(categoryId, dateTime, appId);
	}
	
	
	
	/**
	 * 根据模块ID查询商品信息带分页
	 * @param appId 应用ID
	 * @param modelId 模块ID
	 * @param categoryId 分类ID
	 * @param begin 分页开始位置
	 * @param end 分页结束位置
	 * @return 商品集合
	 */
	public List<ProductEntity> queryPageByModelId(Integer appId,Integer modelId,Integer categoryId,PageUtil page){
		if(categoryId == 0){
			categoryId = null;
		}
		return this.productDao.queryPageByModelId(appId,modelId,categoryId,page.getPageNo()*page.getPageSize(), page.getPageSize());
	}
	
	/**
	 * 根据模块ID查询商品总数
	 * @param appId 应用Id
	 * @param modelId 模块ID
	 * @param categoryId 分类ID
	 * @return 商品总数
	 */
	public int queryCountByModelId(Integer appId,Integer modelId,Integer categoryId){
		if(categoryId == 0){
			categoryId = null;
		}		
		return this.productDao.queryCountByModelId(appId,modelId,categoryId);
	}
	
	@Override
	public List<ProductEntity> queryList(int appId,
			int[] basicCategoryIds, 
			String orderBy, boolean order,Integer productShelf,String flag,String noFlag) {
		// TODO Auto-generated method stub
		if (orderBy!=null) {
			if (orderBy.equals("sort")) {
				orderBy = "basic_sort";
			} else if (orderBy.equals("date")) {
				orderBy = "basic_datetime";
			} else if (orderBy.equals("hit")) {
				orderBy = "basic_hit";
			} else if (orderBy.equals("updatedate")) {
				orderBy = "basic_updatedate";
			} else if (orderBy.equals("id")) {
				orderBy = "basic_id";
			}else if (orderBy.equals("price")){
				orderBy = "basic_id";
			}else if (orderBy.equals("sale")){
				orderBy = "product_sale";
			}else{
				orderBy=null;
			}
		}
		return productDao.queryList(appId, basicCategoryIds,  orderBy, order, productShelf>=0?productShelf:null,flag==null?null:flag.split(","),noFlag==null?null:noFlag.split(","));
	}

	@Override
	public int getSearchCount(ContentModelEntity contentModel,Map wherMap, int websiteId,List  ids) {
		if (contentModel!=null) {
			return productDao.getSearchCount(contentModel.getCmTableName(),wherMap, websiteId,ids);
		}
		return productDao.getSearchCount(null,wherMap, websiteId,ids);
	}

	@Override
	public List<ProductEntity> queryListForSearch(
			ContentModelEntity conntentModel, Map whereMap, PageUtil page,
			int appId, List ids, Map orders) {
		List<ProductEntity> porductList = new ArrayList<ProductEntity>();
		int start = 0;
		int end = 0;
		String tableName = null;
		if (page!=null) {
			start = page.getPageNo();
			end = page.getPageSize();
		}
		if (conntentModel!=null) {
			tableName = conntentModel.getCmTableName();
		}
		// 查找所有符合条件的文章实体
		porductList = productDao.queryListForSearch(tableName, whereMap, start, end, appId,ids,orders);
		

		return porductList;
	}

	@Override
	public List<ProductEntity> queryByShelf(Integer appId,
			Integer productShelf, Integer categoryId,PageUtil page) {
		if(categoryId!=null && categoryId==0){
			categoryId = null;
		}
		// TODO Auto-generated method stub
		return this.productDao.queryPageByShelf(appId, productShelf,categoryId, page.getPageNo(), page.getPageSize());
	}

	@Override
	public int getCountByShelf(Integer appId, Integer productShelf, Integer categoryId) {
		if(categoryId!=null && categoryId==0){
			categoryId = null;
		}
		return productDao.getCountByShelf(appId, productShelf,categoryId);
	}

	
	
	@Override
	public void updateProductShelf(String[] productIds,ProductEnum productShelf) {
		// TODO Auto-generated method stub
		if (!StringUtil.isBlank(productIds)) {
			for (int i = 0; i < productIds.length; i++) {
				int number = Integer.parseInt(productIds[i]);
				// 获取id，查询该文章是否在该站点下
				ProductEntity product = (ProductEntity) this.productDao.getEntity(number);
				product.setProductShelf(productShelf);
				this.productDao.updateEntity(product);
			}
		}
	}
	
	@Override
	public List<ProductEntity> queryProducntSpecificationList(Integer appId,int[] basicCategoryIds,int begin,int count,String orderBy,boolean order,String flag,String noFlag){
		if(basicCategoryIds==null){
			return this.productDao.queryProducntSpecificationList(appId,null,begin, count, orderBy, order,flag,noFlag);
		}
		return this.productDao.queryProducntSpecificationList(appId,basicCategoryIds,begin, count, orderBy, order,flag,noFlag);
	}

	@Override
	public int getProducntSpecificationCount(int[] basicCategoryIds,
			Integer appId,String flag ,String noFlag) {
		// TODO Auto-generated method stub
		if(basicCategoryIds==null){
			return this.productDao.getProducntSpecificationCount(null, appId,flag,noFlag);
		}
		return this.productDao.getProducntSpecificationCount(basicCategoryIds, appId,flag,noFlag);
	}

	@Override
	public List<ProductEntity> queryProducntSpecificationByDateAndByColumnId(
			String dateTime, Integer appId, Integer categoryId) {
		// TODO Auto-generated method stub
		return this.productDao.queryProducntSpecificationByDateAndByColumnId(dateTime, appId, categoryId);
	}

	@Override
	public List<ProductEntity> queryProducntSpecificationForSearch(
			ContentModelEntity conntentModel, Map<String, List> map, int appId, List ids,
			Map sortMap, PageUtil page,String orderBy,boolean order,String flag,String noFlag) {
		// TODO Auto-generated method stub
		String tableName = null;
		if (conntentModel!=null) {
			tableName = conntentModel.getCmTableName();
		}
		return this.productDao.queryProducntSpecificationForSearch(tableName, map, appId, ids, sortMap, page,orderBy,order,flag,noFlag);
	}
	
	@Override
	public int getProducntSpecificationSearchCount(ContentModelEntity contentModel,Map wherMap, int websiteId,List  ids,String flag,String noFlag) {
		if (contentModel!=null) {
			return productDao.getProducntSpecificationSearchCount(contentModel.getCmTableName(),wherMap, websiteId,ids,flag, noFlag);
		}
		return productDao.getProducntSpecificationSearchCount(null,wherMap, websiteId,ids, flag, noFlag);
	}

	
	@Override
	public List<ProductEntity> querySearch(int appId, int categoryId,Map map) {
		// TODO Auto-generated method stub
		return productDao.querySearch(appId, categoryId, map);
	}

	@Override
	public int getCountByDiyField(int appId, Integer categoryId,
			Map productMap, Map diyFieldMap,String tableName) {
		// TODO Auto-generated method stub
		return this.productDao.getCountByDiyField(appId, categoryId, productMap, diyFieldMap,tableName);
	}

	@Override
	public List<ProductEntity> queryByDiyField(int appId, Integer categoryId,
			Map map, Map diyFieldMap, PageUtil page, String tableName) {
		// TODO Auto-generated method stub
		return this.productDao.queryByDiyField(appId, categoryId, map, diyFieldMap, page, tableName);
	}

	@Override
	public List<ProductEntity> queryAllShelf(Integer appId,
			Integer productShelf, Integer categoryId) {
		// TODO Auto-generated method stub
		return this.productDao.queryAllShelf(appId, productShelf, categoryId);
	}

	@Override
	public List queryByCategoryForBean(int appId, Integer categoryId,
			PageUtil page, boolean _isHasChilds) {
		// TODO Auto-generated method stub
		ModelEntity model=modelBiz.getEntityByModelCode(net.mingsoft.mall.constant.ModelCode.MALL_CATEGORY);
		//查询该分类下的所有子分类
		List list = categoryBiz.queryChildrenCategory(categoryId,appId,model.getModelId());
		// 分类不存在直接返回
		if (list == null || list.size() == 0) {
						return null;
		}
		//如果没有子分类
		if(list.size()==1){
			ColumnEntity column = (ColumnEntity) columnBiz.getEntity(categoryId);
				if (column.getColumnContentModelId() != 0) { // 存在自定义模型
					ContentModelEntity contentModel = (ContentModelEntity) contentModelBiz.getEntity(column.getColumnContentModelId());
						return productDao.queryByCategoryForBean(categoryId, null, page, contentModel.getCmTableName());
					} else {
						return productDao.queryByCategoryForBean(categoryId, null, page, null);
					}
				}else{
					if (_isHasChilds) {
						return productDao.queryByCategoryForBean(categoryId, list, page,null);
					} else {
						return productDao.queryByCategoryForBean(categoryId, null, page,null);
					}
		}
	}

	@Override
	public void saveProduct(ProductEntity product, List<BasicCategoryEntity> basicCategoryList) {
		// TODO Auto-generated method stub
		this.saveBasic(product);
		List<BasicCategoryEntity> newBasicCategoryList = new ArrayList<BasicCategoryEntity>();
		for(int i=0;i<basicCategoryList.size();i++){
			BasicCategoryEntity basicCategory= basicCategoryList.get(i);
			basicCategory.setBcBasicId(product.getBasicId());
			newBasicCategoryList.add(basicCategory);
			
		}
		if(newBasicCategoryList==null || newBasicCategoryList.size()<=0){
			return;
		}
		this.basicCategoryBiz.deleteEntity(product.getBasicId());
		this.basicCategoryBiz.saveBatch(newBasicCategoryList);
	}

	@Override
	public void updateProduct(ProductEntity product, List<BasicCategoryEntity> basicCategoryList) {
		// TODO Auto-generated method stub
		this.updateBasic(product);
		if(basicCategoryList==null || basicCategoryList.size()<=0){
			return;
		}
		if(product!=null){
			this.basicCategoryBiz.deleteEntity(product.getBasicId());
			this.basicCategoryBiz.saveBatch(basicCategoryList);
		}
		
	}

	@Override
	public List<ProductEntity> queryByBasicIds(int appId,Integer categoryId,List<Integer> basicIds, PageUtil page, String orderBy, boolean order,Integer productShelf) {
		if (orderBy!=null) {
			if (orderBy.equals("sort")) {
				orderBy = "basic_sort";
			} else if (orderBy.equals("date")) {
				orderBy = "basic_datetime";
			} else if (orderBy.equals("hit")) {
				orderBy = "basic_hit";
			} else if (orderBy.equals("updatedate")) {
				orderBy = "basic_updatedate";
			} else if (orderBy.equals("id")) {
				orderBy = "basic_id";
			}else if (orderBy.equals("price")){
				orderBy = "basic_id";
			}else if (orderBy.equals("sale")){
				orderBy = "product_sale";
			}else{
				orderBy=null;
			}
		}
		return this.productDao.queryByBasicIds(appId,categoryId,basicIds, page, orderBy, order,productShelf);
	}

	@Override
	public int getCountByBasicIds(int appId, Integer categoryId, List<Integer> basicIds, Integer productShelf) {
		// TODO Auto-generated method stub
		return this.productDao.getCountByBasicIds(appId, categoryId, basicIds, productShelf);
	}

	@Override
	public void delete(int appId, int[] ids) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected IBaseDao getDao() {
		// TODO Auto-generated method stub
		return productDao;
	}
	
}