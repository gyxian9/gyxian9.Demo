package weico.gyx.org.person_comment_client.entity;

/**
 * Created by gyx on 2015/10/27.
 */
public class City {
    private String name;
    private String pinyin;

    public City(){

    }

    public City(String name,String pinyin){
        this.name = name;
        this.pinyin = pinyin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getPinyin() {
        return pinyin;
    }
}
