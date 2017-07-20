package bdkj.com.englishstu.common.beans;

/**
 * author:davidinchina on 2017/7/20 17:24
 * email:davicdinchina@gmail.com
 * tip:
 */
public class MarkResult {
    private String content;
    private String score;

    public MarkResult(String content, String score) {
        this.content = content;
        this.score = score;
    }

    public MarkResult() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
