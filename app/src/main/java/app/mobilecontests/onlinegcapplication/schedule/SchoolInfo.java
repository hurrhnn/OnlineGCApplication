package app.mobilecontests.onlinegcapplication.schedule;

public enum SchoolInfo {
    AUTH_KEY("e3dffc501f6b42c88900e6ac3139ca60"),
    BASE_URL("https://open.neis.go.kr/hub/");

    final private String value;

    SchoolInfo(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
