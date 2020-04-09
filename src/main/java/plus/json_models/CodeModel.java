package plus.json_models;

public class CodeModel {
    public String result;
    public String code;

    public CodeModel(String result, String code) {
        this.result = result;
        this.code = code;
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
