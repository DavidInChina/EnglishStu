package bdkj.com.englishstu.common.eventbus;

/**
 * Created by davidinchina on 2017/7/5.
 */

public class ChangeMarkType {
    private String type;

    public ChangeMarkType(String type) {
        this.type = type;
    }

    public ChangeMarkType() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
