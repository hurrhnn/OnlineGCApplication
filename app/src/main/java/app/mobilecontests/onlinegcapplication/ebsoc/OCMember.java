package app.mobilecontests.onlinegcapplication.ebsoc;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import app.mobilecontests.onlinegcapplication.utils.HTTPRequestUtils;

public class OCMember {

    public Map<String, String> loginCookie = new HashMap<>();

    private String raw;
    private String token;

    private int memberSeq;
    private String memberClassifyCode;
    private String memberOfficeEdu;
    private String memberTypeCode;
    private String memberName;
    private String memberEmail;
    private String memberDivisionCode;

    private OCSchool ocSchool;

    public Map<String, String> getLoginCookie() {
        return loginCookie;
    }

    private void setLoginCookie() {
        loginCookie.put("access", getToken());
        loginCookie.put("host", ocSchool.getHostName());
        loginCookie.put("memberSchoolCode", ocSchool.getMemberTargetCode());
        loginCookie.put("memberSeq", String.valueOf(getMemberSeq()));
        loginCookie.put("memberTargetCode", String.valueOf(ocSchool.getMemberTargetCode()));
        loginCookie.put("schoolInfoYn", "Y");
    }

    @NotNull
    @Override
    public String toString() {
        return raw;
    }

    public String getToken() {
        return token;
    }

    public int getMemberSeq() {
        return memberSeq;
    }

    public String getMemberClassifyCode() {
        return memberClassifyCode;
    }

    public String getMemberOfficeEdu() {
        return memberOfficeEdu;
    }

    public String getMemberTypeCode() {
        return memberTypeCode;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public OCSchool getOCSchool() {
        return ocSchool;
    }

    public String getMemberDivisionCode() {
        return memberDivisionCode;
    }

    public Exception setOCMember(String memberId, String memberPw) {
        HTTPRequestUtils HTTPRequestUtils = new HTTPRequestUtils();

        Map<String, String> data = new HashMap<>();
        data.put("memberId", memberId);
        data.put("memberPassword", memberPw);

        try {
            Connection.Response loginResponse = HTTPRequestUtils.POST(OCInfo.LOGIN_API_URL.getValue(), new HashMap<>(), new JSONObject(data).toString(), new String[]{"X-AUTH-TOKEN", null});

            JSONObject jsonLoginResponse = new JSONObject(loginResponse.body());
            JSONObject jsonResponseMemberData = jsonLoginResponse.getJSONObject("data").getJSONObject("memberInfo");

            raw = jsonLoginResponse.toString(4);
            token = jsonLoginResponse.getJSONObject("data").getString("token");

            memberSeq = jsonResponseMemberData.getInt("memberSeq");
            memberClassifyCode = jsonResponseMemberData.getString("memberClassifyCode");
            memberOfficeEdu = jsonResponseMemberData.getString("memberOfficeEdu");
            memberTypeCode = jsonResponseMemberData.getString("memberTypeCode");
            memberName = jsonResponseMemberData.getString("memberName");

            memberEmail = jsonResponseMemberData.getString("memberEmail");
            ocSchool = new OCSchool(jsonResponseMemberData.getJSONObject("memberSchoolInfo"));
            memberDivisionCode = jsonResponseMemberData.getString("memberDivisionCode");

            setLoginCookie();
            return null;
        } catch (HttpStatusException httpStatusException) {
            return new HttpStatusException("EBS Online Class server has returned code " + httpStatusException.getStatusCode(), httpStatusException.getStatusCode(), httpStatusException.getUrl());
        } catch (SocketTimeoutException socketTimeoutException) {
            return new SocketTimeoutException("Server Connection TIME OUT.");
        } catch (Exception e) {
            return e;
        }
    }

    public static class OCSchool {
        final private String raw;

        final private String schoolTypeCode;
        final private String schoolTypeCodeNm;
        final private String hostName;
        final private String schoolCode;
        final private String schoolCodeNm;
        final private String memberSchoolClassName;
        final private String memberTargetCode;
        final private String memberTargetCodeNm;

        final private int memberSchoolClassNo;

        private int memberSchoolGrdNumber = 0;

        public String getSchoolTypeCode() {
            return schoolTypeCode;
        }

        public String getSchoolTypeCodeNm() {
            return schoolTypeCodeNm;
        }

        public String getHostName() {
            return hostName;
        }

        public String getSchoolCodeNm() {
            return schoolCodeNm;
        }

        public int getMemberSchoolClassNo() {
            return memberSchoolClassNo;
        }

        public String getMemberSchoolClassName() {
            return memberSchoolClassName;
        }

        public String getMemberTargetCode() {
            return memberTargetCode;
        }

        public String getMemberTargetCodeNm() {
            return memberTargetCodeNm;
        }

        public String getSchoolCode() {
            return schoolCode;
        }

        public int getMemberSchoolGrdNumber() {
            return memberSchoolGrdNumber;
        }

        public OCSchool(JSONObject jsonMemberSchool) throws JSONException {
            raw = jsonMemberSchool.toString(4);
            schoolTypeCode = jsonMemberSchool.getString("schoolTypeCode");
            schoolTypeCodeNm = jsonMemberSchool.getString("schoolTypeCodeNm");
            hostName = jsonMemberSchool.getString("hostName");
            schoolCodeNm = jsonMemberSchool.getString("schoolCodeNm");
            memberSchoolClassNo = jsonMemberSchool.getInt("memberSchoolClassNo");
            memberSchoolClassName = jsonMemberSchool.getString("memberSchoolClassName");
            memberTargetCode = jsonMemberSchool.getString("memberTargetCode");
            memberTargetCodeNm = jsonMemberSchool.getString("memberTargetCodeNm");
            schoolCode = jsonMemberSchool.getString("schoolCode");

            for (int i = 1; i <= 6; i++) {
                if (jsonMemberSchool.getString("memberSchoolGrd" + i + "Yn").equals("Y")) {
                    memberSchoolGrdNumber = i;
                    break;
                }
            }
        }

        @NotNull
        @Override
        public String toString() {
            return raw;
        }
    }
}
