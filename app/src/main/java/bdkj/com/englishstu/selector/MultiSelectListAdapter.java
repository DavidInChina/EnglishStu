package bdkj.com.englishstu.selector;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davidecirillo.multichoicerecyclerview.MultiChoiceAdapter;

import java.util.ArrayList;

import bdkj.com.englishstu.R;

/**
 * ================================================
 * 作    者：JayGoo
 * 版    本：1.1.0
 * 创建日期：2017/2/21
 * 描    述:多选适配器
 * ================================================
 */
public class MultiSelectListAdapter extends MultiChoiceAdapter<MultiSelectListAdapter.ChoiceViewHolder> {

    private ArrayList<ChooseData> choiceData = new ArrayList<>();
    private boolean singleChoose;
//    private Boolean[] selectStates;
    private OnSelectChangeListener mOnSelectChangeListener;
    private OnSelectAllListener mOnSelectAllListener;

    /**
     * when you select the choices,it will be called
     */
    public interface OnSelectChangeListener{
        void onChanged(ArrayList<ChooseData> dataList, int selectedNumber);
    }

    /**
     * it will be auto called when you select all choices
     */
    public interface OnSelectAllListener{
        void onChanged(boolean isSelectedAll);
    }

    public MultiSelectListAdapter(ArrayList<ChooseData> list,OnSelectChangeListener listener,boolean singleChoose){
        choiceData = list;
        mOnSelectChangeListener = listener;
        this.singleChoose = singleChoose;
        if (singleChoose){
            for (ChooseData data1:choiceData
                    ) {
                data1.setChoose(false);
            }
        }
    }

    @Override
    public ChoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChoiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.muli_select_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(final ChoiceViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
         ChooseData data = choiceData.get(position);
        // change the state
        holder.choiceNameBtn.setText(data.getShowText());
        holder.choiceNameBtn.setSelected(data.isChoose());
        if (mOnSelectChangeListener != null){//这里用来显示默认选中内容的数目
            mOnSelectChangeListener.onChanged(getSelectedData(),getSelectedNumber());
        }
        holder.choiceNameBtn.setTag(data);
        holder.choiceNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!singleChoose||getSelectedData().size()<1){
                    ChooseData data1 = (ChooseData) v.getTag();
                    data1.setChoose(!data1.isChoose());
                    holder.choiceNameBtn.setSelected(data1.isChoose());
                    if (mOnSelectChangeListener != null){
                        mOnSelectChangeListener.onChanged(getSelectedData(),getSelectedNumber());
                    }
                    if (mOnSelectAllListener != null){
                        int num = getSelectedNumber();
                        if (num == choiceData.size() && choiceData.size() >0){
                            mOnSelectAllListener.onChanged(true);
                        } else {
                            mOnSelectAllListener.onChanged(false);
                        }
                    }
                }

            }
        });

    }

//
//    @Override
//    protected View.OnClickListener defaultItemViewClickListener(final ChoiceViewHolder holder, final int position) {
//        ChooseData data = choiceData.get(position);
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectStates[position] = !selectStates[position];
//                holder.choiceNameBtn.setSelected(selectStates[position]);
//                if (mOnSelectChangeListener != null){
//                    mOnSelectChangeListener.onChanged(getSelectedData(),getSelectedNumber());
//                }
//
//                if (mOnSelectAllListener != null){
//                    int num = getSelectedNumber();
//                    if (num == choiceData.size() && choiceData.size() >0){
//                        mOnSelectAllListener.onChanged(true);
//                    } else {
//                        mOnSelectAllListener.onChanged(false);
//                    }
//                }
//            }
//        };
//    }

    @Override
    public int getItemCount() {
        return choiceData.size();
    }

    /**
     * get the indexes in an array which you had selected
     * @return
     */
    public ArrayList<ChooseData> getSelectedData(){
        ArrayList<ChooseData> result = new ArrayList<>();
        if (choiceData == null) {
            return result;
        }
            for (ChooseData data:choiceData
                    ) {
                if (data.isChoose())
                result.add(data);
            }
        return result;
    }

    /**
     * get the size of you had selected
     *
     * @return
     */
    public int getSelectedNumber(){
        if (singleChoose){
            return 0;
        }
        return getSelectedData().size();
    }

    /**
     * select all and change the states
     */
    public void selectAll(){
        if (choiceData != null) {
            for (ChooseData data:choiceData
                    ) {
                data.setChoose(true);
            }
            notifyDataSetChanged();
            if (mOnSelectChangeListener != null){
                mOnSelectChangeListener.onChanged(getSelectedData(),getSelectedNumber());
            }
        }
    }

    /**
     * cacel select all and change the states
     */
    public void cancelAll(){
        if (choiceData != null) {
            for (ChooseData data:choiceData
                 ) {
                data.setChoose(false);
            }
            notifyDataSetChanged();
            if (mOnSelectChangeListener != null){
                mOnSelectChangeListener.onChanged(getSelectedData(),getSelectedNumber());
            }
        }
    }




    public void setOnSelectAllListener(OnSelectAllListener listener){
        mOnSelectAllListener = listener;
    }


    /**
     * viewHolder
     */
    public class ChoiceViewHolder extends RecyclerView.ViewHolder {

        public TextView choiceNameBtn;

        public ChoiceViewHolder(View itemView) {
            super(itemView);
            choiceNameBtn = (TextView) itemView.findViewById(R.id.choiceNameBtn);
        }
    }
}
