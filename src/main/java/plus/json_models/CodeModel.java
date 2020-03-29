package plus.json_models;

public class CodeModel {
    public String result;
    public String code;

    @Override
    public String toString() {
        return "CodeModel{" +
                "result='" + result + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    public void rectify() {
        this.code = this.code.replace("['", "").replace("']", "");
    }
}
