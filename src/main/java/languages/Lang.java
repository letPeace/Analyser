package languages;

public enum Lang{

    RUSSIAN("RU"),
    ENGLISH("EN"),
    NUMBER("NU"),
    PUNCTUATION_MARK("PM"),
    UNKNOWN("UN");

    private final String code;

    Lang(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
