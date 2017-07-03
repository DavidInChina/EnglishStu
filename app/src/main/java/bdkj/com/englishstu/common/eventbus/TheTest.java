package bdkj.com.englishstu.common.eventbus;

import bdkj.com.englishstu.common.beans.Test;

/**
 * Created by davidinchina on 2017/7/3.
 * 当前所选择的试题
 */

public class TheTest {
    private Test test;

    public TheTest(Test test) {
        this.test = test;
    }

    public TheTest() {
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }
}
