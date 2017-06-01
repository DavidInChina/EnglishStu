package bdkj.com.englishstu.base;

/**
 * 基础响应数据结构模型
 * <p>
 * Created by zhangfei on 2017/3/22.
 */

public class JsonEntity<T> {

    private int code;
    private String msg;
    private T data;

    public JsonEntity() {
        code = 1;//默认返回失败结果
        msg = "数据交互失败！";
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
