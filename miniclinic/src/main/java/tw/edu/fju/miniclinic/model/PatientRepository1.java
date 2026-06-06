package tw.edu.fju.miniclinic.model;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class PatientRepository1 {

    // 虛構的病患資料
    private static final List<Patient1> PATIENTS = Arrays.asList(
        new Patient1("TEST00001", "王韋澔", "男", "0966-852-517"),
        new Patient1("TEST00002", "李彥諄", "男", "0979-200-305"),
        new Patient1("TEST00003", "吳宇涵", "女", "0988-540-334")
    );

    /**
     * 取得所有病患資料
     */
    public List<Patient1> findAll() {
        return PATIENTS;
    }

    /**
     * 根據病歷號 (chartNo) 查詢病患
     */
    public Optional<Patient1> findByChartNo(String chartNo) {
        return PATIENTS.stream()
            .filter(p -> p.getChartNo().equals(chartNo))
            .findFirst();
    }
}