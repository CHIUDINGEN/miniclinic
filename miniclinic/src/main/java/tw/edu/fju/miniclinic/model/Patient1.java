package tw.edu.fju.miniclinic.model;

public class Patient1 {
    private String chartNo;   // 病歷號
    private String name;      // 姓名
    private String gender;    // 性別
    private String phone;     // 聯絡電話

    public Patient1(String chartNo, String name, String gender, String phone) {
        this.chartNo = chartNo;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
    }

    // Getters
    public String getChartNo() { return chartNo; }
    public String getName() { return name; }
    public String getGender() { return gender; }
    public String getPhone() { return phone; }

    // Setters
    public void setChartNo(String chartNo) { this.chartNo = chartNo; }
    public void setName(String name) { this.name = name; }
    public void setGender(String gender) { this.gender = gender; }
    public void setPhone(String phone) { this.phone = phone; }
}