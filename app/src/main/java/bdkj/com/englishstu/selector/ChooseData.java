package bdkj.com.englishstu.selector;

/**
 * Created by Administrator on 2017/6/21.
 */

public class ChooseData {
    private String showText;
    private String chooseDate;
    private boolean isChoose;

    public ChooseData(String showText, String chooseDate, boolean isChoose) {
        this.showText = showText;
        this.chooseDate = chooseDate;
        this.isChoose = isChoose;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public ChooseData(String showText, String chooseDate) {
        this.showText = showText;
        this.chooseDate = chooseDate;
    }

    public ChooseData() {
    }

    public String getShowText() {
        return showText;
    }

    public void setShowText(String showText) {
        this.showText = showText;
    }

    public String getChooseDate() {
        return chooseDate;
    }

    public void setChooseDate(String chooseDate) {
        this.chooseDate = chooseDate;
    }
}
