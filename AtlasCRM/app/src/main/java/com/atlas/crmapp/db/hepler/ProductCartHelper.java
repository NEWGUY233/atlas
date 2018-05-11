package com.atlas.crmapp.db.hepler;

import com.atlas.crmapp.db.greendao.ProductCartDao;
import com.atlas.crmapp.db.model.ProductCart;

import java.util.List;

/**
 * Created by hoda on 2017/7/6.
 */

public class ProductCartHelper {

    public static List<ProductCart> getAllProduct(long unitId) {
        return GreenDaoManager.getInstance().getmDaoSession().getProductCartDao().queryBuilder().where(ProductCartDao.Properties.UnitId.eq(unitId)).list();
    }


    public static ProductCart getProduct(long productId, long unitId) {
        return GreenDaoManager.getInstance().getmDaoSession().getProductCartDao().queryBuilder()
                .where(ProductCartDao.Properties.ProductId.eq(productId),ProductCartDao.Properties.UnitId.eq(unitId)).unique();
    }

    public static void updateProduct(long productId, int num, String bizCode, long unitid) {
        ProductCart dbProductCart =  getProduct(productId, unitid);
        if(dbProductCart == null){
            insertProduct(productId, num, bizCode, unitid);
        }else {
            ProductCart productCart = new ProductCart();
            productCart.setProductId(productId);
            productCart.setNum(num);
            productCart.setBizCode(bizCode);
            productCart.setUnitId(unitid);
            productCart.setId(dbProductCart.getId());
            GreenDaoManager.getInstance().getmDaoSession().getProductCartDao().update(productCart);
        }
    }

    public static  void insertProduct(long productId, int num, String bizCode, long unitId){
        ProductCart productCart = new ProductCart();
        productCart.setBizCode(bizCode);
        productCart.setProductId(productId);
        productCart.setNum(num);
        productCart.setUnitId(unitId);
        GreenDaoManager.getInstance().getmDaoSession().getProductCartDao().insert(productCart);
    }


    public static void removeProduct( long productId, long unitId) {
        GreenDaoManager.getInstance().getmDaoSession().getProductCartDao().delete(getProduct(productId, unitId));
    }

    public static void removeAllWidthUnitId(long unitId) {
        ProductCartDao productCartDao = GreenDaoManager.getInstance().getmDaoSession().getProductCartDao();
        List<ProductCart> productCart = productCartDao.queryBuilder().where(ProductCartDao.Properties.UnitId.eq(unitId)).list();
        productCartDao.deleteInTx(productCart);
    }

}
