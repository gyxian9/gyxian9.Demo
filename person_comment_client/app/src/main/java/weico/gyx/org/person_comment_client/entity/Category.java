package weico.gyx.org.person_comment_client.entity;

/**
 * Created by gyx on 2015/9/5.
 */
public class Category {

    private int categoryId;
    private int categoryNumber;

    public Category(){

    }

    public Category(int categoryId,int categoryNumber){
        this.categoryId = categoryId;
        this.categoryNumber = categoryNumber;
    }

    public int getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    public int getCategoryNumber() {
        return categoryNumber;
    }
    public void setCategoryNumber(int categoryNumber) {
        this.categoryNumber = categoryNumber;
    }

    @Override
    public String toString() {
        return categoryId+" --> "+categoryNumber;
    }
}
