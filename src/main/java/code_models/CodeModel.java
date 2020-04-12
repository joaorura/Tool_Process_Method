package code_models;

public class CodeModel {
    public String result;
    public String code;

    public CodeModel(String name, String theCode) {
        this.result = name;
        this.code = theCode;
    }

    @Override
    public String toString() {
        return "CodeModel{" +
                "result='" + result + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    public void rectify() {
        this.result = this.result.replace("['", "").replace("']", "");
    }
}
