package weico.gyx.org.person_comment_client.entity;

/**
 * Created by gyx on 2015/9/6.
 */
public class ResponseObject<T> {
    private T data;
    private int page;
    private int size;
    private int count;

    private int state;
    private String msg;

    public ResponseObject(T data){
        this.data = data;
    }
    public ResponseObject(){

    }


    public T getData() {
        return data;
    }
    public void setDatas(T data) {
        this.data = data;
    }
    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
