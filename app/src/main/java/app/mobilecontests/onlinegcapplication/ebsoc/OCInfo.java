package app.mobilecontests.onlinegcapplication.ebsoc;

public enum OCInfo {
    LOGIN_API_VER("1"),
    LOGIN_API_URL("https://ebsoc.co.kr/auth/api/v" + LOGIN_API_VER.getValue() + "/login"),
    LEARNING_API_URL("https://%s.ebsoc.co.kr/lecture/api/v" + LOGIN_API_VER.getValue() + "/student/learning");

    final private String value;

    OCInfo(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}