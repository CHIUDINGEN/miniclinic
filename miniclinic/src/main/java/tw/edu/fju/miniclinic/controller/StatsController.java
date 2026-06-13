package tw.edu.fju.miniclinic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import tw.edu.fju.miniclinic.model.Appointment;
import tw.edu.fju.miniclinic.model.AppointmentRepository;
import tw.edu.fju.miniclinic.model.DoctorRepository;
import tw.edu.fju.miniclinic.model.PatientRepository;

import java.util.List;
import java.util.Objects;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class StatsController {

    private final DoctorRepository doctorRepo;
    private final PatientRepository patientRepo;
    private final AppointmentRepository appointmentRepo;

    public StatsController(DoctorRepository doctorRepo, 
                           PatientRepository patientRepo, 
                           AppointmentRepository appointmentRepo) {
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
        this.appointmentRepo = appointmentRepo;
    }

    /**
     * GET /stats
     * 顯示統計數據網頁
     */
    @GetMapping("/stats")
    public String statsPage(Model model) {
        // 1. 取得基礎統計總量
        model.addAttribute("doctorCount", doctorRepo.count());
        model.addAttribute("patientCount", patientRepo.count());
        model.addAttribute("appointmentCount", appointmentRepo.count());

        // 2. 取得科別掛號分佈統計 (根據掛號資料中的醫師所屬科別進行分組)
        List<Appointment> appointments = appointmentRepo.findAll();
        Map<String, Long> deptStatsMap = appointments.stream()
                .map(a -> a.getDoctor().getDepartment())
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(d -> d, Collectors.counting()));

        // 轉為模板預期的 List<Object[]> 格式 (stat[0]=名稱, stat[1]=數量)
        List<Object[]> deptStats = deptStatsMap.entrySet().stream()
                .map(e -> new Object[]{e.getKey(), e.getValue()})
                .collect(Collectors.toList());

        model.addAttribute("deptStats", deptStats);
        return "stats";
    }

    /**
     * GET /api/stats
     * 回傳系統統計摘要，包含醫師、病患總數及掛號狀態統計。
     */
    @GetMapping("/api/stats")
    @ResponseBody
    public Map<String, Object> getStats() {
        // 取得所有掛號資料以進行狀態分類統計
        List<Appointment> appointments = appointmentRepo.findAll();

        // 使用 Stream API 依照狀態進行分組計數（過濾掉 null 以防 NPE）
        Map<String, Long> byStatus = appointments.stream()
                .map(Appointment::getStatus)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));

        // 確保 BOOKED, COMPLETED, CANCELLED 即使為 0 也會出現在結果中
        Map<String, Long> statusCounts = Map.of(
                "BOOKED", byStatus.getOrDefault("BOOKED", 0L),
                "COMPLETED", byStatus.getOrDefault("COMPLETED", 0L),
                "CANCELLED", byStatus.getOrDefault("CANCELLED", 0L)
        );

        return Map.of(
                "totalDoctors", doctorRepo.count(),
                "totalPatients", patientRepo.count(),
                "totalAppointments", (long) appointments.size(),
                "byStatus", statusCounts
        );
    }
}