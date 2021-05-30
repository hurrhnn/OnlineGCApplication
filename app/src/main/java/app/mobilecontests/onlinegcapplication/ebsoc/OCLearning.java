package app.mobilecontests.onlinegcapplication.ebsoc;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OCLearning {
    private final String raw;
    private final List<JSONObject> learningList = new ArrayList<>();

    @NotNull
    @Override
    public String toString() {
        return raw;
    }

    public List<JSONObject> getLearningList() {
        return learningList;
    }
    public JSONObject getLearningByIndex(int index) {
        return learningList.get(index);
    }

    public int getLearningCount() {
        return learningList.size();
    }

    public int getLearningUspsSqNo(int index) throws JSONException {
        return learningList.get(index).getInt("uspsSqno");
    }

    public String getLearningClassNm(int index) throws JSONException {
        return learningList.get(index).getString("classNm");
    }

    public int getLearningClassSqNo(int index) throws JSONException {
        return learningList.get(index).getInt("classSqno");
    }

    public String getLearningClassUrlPath(int index) throws JSONException {
        return learningList.get(index).getString("classUrlPath");
    }

    public int getLearningLsnCreaseSqNo(int index) throws JSONException {
        return learningList.get(index).getInt("lsnCrseSqno");
    }

    public String getLearningLsnCreaseNm(int index) throws JSONException {
        return learningList.get(index).getString("lsnCrseNm");
    }

    public int getLearningLsnSqNo(int index) throws JSONException {
        return learningList.get(index).getInt("lsnSqno");
    }

    public String getLearningLsnNm(int index) throws JSONException {
        return learningList.get(index).getString("lsnNm");
    }

    public String getLearningFirstRegisterDt(int index) throws JSONException {
        return learningList.get(index).getString("frstRgstDt");
    }

    public String getLearningLrnBgnDt(int index) throws JSONException {
        return learningList.get(index).getString("lrnBgnDt");
    }

    public String getLearningLrnEndDt(int index) throws JSONException {
        return learningList.get(index).getString("lrnEndDt");
    }

    public int getLearningRtPgsRt(int index) throws JSONException {
        return learningList.get(index).getInt("rtpgsRt");
    }

    public OCLearning(JSONObject jsonLearningListObject) throws JSONException {
        this.raw = jsonLearningListObject.toString(4);

        for(int i = 0; i < jsonLearningListObject.getJSONObject("data").getJSONArray("list").length(); i++) {
            learningList.add((JSONObject) jsonLearningListObject.getJSONObject("data").getJSONArray("list").get(i));
        }
    }
}