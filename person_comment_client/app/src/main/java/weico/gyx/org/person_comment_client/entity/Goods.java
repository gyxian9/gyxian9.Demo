package weico.gyx.org.person_comment_client.entity;

import java.io.Serializable;

/**
 * Created by gyx on 2015/9/5.
 */
public class Goods implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;//商品ID
    private String categoryId;//分类ID
    private String title;//商品名称
    private String sortTitle;//商品描述信息
    private String imgUrl;//商品的图片
    private String value;//商品原价
    private String price;//商品销售价
    private String detail;//描述详情


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getSortTitle() {
        return sortTitle;
    }
    public void setSortTitle(String sortTitle) {
        this.sortTitle = sortTitle;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getDetail() {
        return detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }

}
