package tw.edu.fju.miniclinic.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tw.edu.fju.miniclinic.model.Appointment;
import tw.edu.fju.miniclinic.model.AppointmentRepository;
import tw.edu.fju.miniclinic.model.DoctorRepository;
import tw.edu.fju.miniclinic.model.PatientRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
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
     * GET /api/stats
     * 回傳系統統計摘要，包含醫師、病患總數及掛號狀態統計。
     */
    @GetMapping("/api/stats")
    public Map<String, Object> getStats() {
        // 取得所有掛號資料以進行狀態分類統計
        List<Appointment> appointments = appointmentRepo.findAll();

        // 使用 Stream API 依照狀態進行分組計數
        Map<String, Long> byStatus = appointments.stream()
                .collect(Collectors.groupingBy(
                        Appointment::getStatus,
                        Collectors.counting()
                ));

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