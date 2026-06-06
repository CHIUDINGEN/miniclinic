package tw.edu.fju.miniclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import tw.edu.fju.miniclinic.model.Appointment;
import tw.edu.fju.miniclinic.model.AppointmentForm;
import tw.edu.fju.miniclinic.model.AppointmentRepository;
import tw.edu.fju.miniclinic.model.Doctor;
import tw.edu.fju.miniclinic.model.DoctorRepository;
import tw.edu.fju.miniclinic.model.Patient;
import tw.edu.fju.miniclinic.model.PatientRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class AppointmentController {

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    // GET：顯示表單
    @GetMapping("/appointment/new")
    public String newAppointmentForm(Model model) {
        model.addAttribute("form", new AppointmentForm());
        model.addAttribute("doctors", doctorRepo.findAll());
        return "appointment-new";
    }

    // POST：接收表單
    @PostMapping("/appointment/new")
    public String submitAppointment(
            @Valid @ModelAttribute("form") AppointmentForm form,   // ← 加 @Valid
            BindingResult result,                          // ← 緊接在 @Valid 參數之後
            Model model) {

        if (result.hasErrors()) {
		    model.addAttribute("form", form);
		    model.addAttribute("doctors", doctorRepo.findAll());
		    return "appointment-new";
	}

        // 步驟 1：用表單的字串 ID，從資料庫查出真正的物件
        Patient patient = patientRepo.findById(form.getChartNo()).orElse(null);
        Doctor  doctor  = doctorRepo.findById(form.getDoctorId()).orElse(null);

        // 步驟 2：驗證——找不到就回表單顯示錯誤
        if (patient == null || doctor == null) {
            model.addAttribute("error", "查無此病歷號或醫師，請確認後重試");
            model.addAttribute("form", form);
            model.addAttribute("doctors", doctorRepo.findAll());
            return "appointment-new";   // ← 回到表單頁，不是跳轉
        }

        // 步驟 3：建立 Appointment Entity，設定關聯物件
        Appointment appt = new Appointment();
        appt.setPatient(patient);
        appt.setDoctor(doctor);
        appt.setApptDate(LocalDate.parse(form.getApptDate()));  // 字串 → LocalDate
        appt.setTimeSlot(form.getTimeSlot());
        appt.setStatus("BOOKED");

        // 步驟 4：存入資料庫，JPA 自動填入 apptId
        Appointment saved = appointmentRepo.save(appt);

        // 步驟 5：把儲存後的物件交給結果頁面
        model.addAttribute("appointment", saved);
        return "appointment-result";
    }

    @PostMapping("/api/appointments")
    public ResponseEntity<Appointment> createAppointment(
    @RequestBody Map<String, String> request) {

	    // 從 request 取出資料
	    String chartNo = request.get("chartNo");
	    String doctorId = request.get("doctorId");
	    LocalDate apptDate = LocalDate.parse(request.get("apptDate"));
	    String timeSlot = request.get("timeSlot");

	    // 查詢關聯的 Patient 與 Doctor
	    Patient patient = patientRepo.findById(chartNo).orElse(null);
	    Doctor doctor = doctorRepo.findById(doctorId).orElse(null);

	    if (patient == null || doctor == null) {
		    return ResponseEntity.badRequest().build();
	    }

	    // 建立 Appointment 物件
	    Appointment appt = new Appointment();
	    appt.setPatient(patient);
	    appt.setDoctor(doctor);
	    appt.setApptDate(apptDate);
	    appt.setTimeSlot(timeSlot);
	    appt.setStatus("BOOKED");

	    Appointment saved = appointmentRepo.save(appt);
	    return ResponseEntity.status(201).body(saved);
    }

    // GET /api/appointments/count：回傳總掛號數
    @GetMapping("/api/appointments/count")
    @ResponseBody
    public Map<String, Long> getAppointmentCount() {
        return Map.of("count", appointmentRepo.count());
    }

    // GET /api/appointments：依條件篩選掛號
    @GetMapping("/api/appointments")
    @ResponseBody
    public List<Appointment> getAppointments(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String doctorId) {

        // 進階建議：如果想同時支援日期「且」醫師篩選，應在此判斷
        // 目前邏輯為：優先選日期，沒日期才選醫師，都沒傳則全列

        if (date != null && !date.isBlank()) {
            return appointmentRepo.findByApptDate(LocalDate.parse(date));
        }

        if (doctorId != null && !doctorId.isBlank()) {
            Doctor doctor = doctorRepo.findById(doctorId).orElse(null);
            if (doctor != null) {
                return appointmentRepo.findByDoctor(doctor);
            }
            return List.of();
        }

        return appointmentRepo.findAll();
    }

    @PutMapping("/api/appointments/{id}/status")
    @ResponseBody // 確保回傳 JSON 而非網頁名稱
    public ResponseEntity<?> updateAppointmentStatus(
            @PathVariable Long id, 
            @RequestBody Map<String, String> body,
            HttpSession session) {
    
    // 安全檢查：必須登入才能操作
    if (session.getAttribute("loggedInDoctorId") == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "請先登入"));
    }
    
    // 尋找該筆掛號
    Appointment appointment = appointmentRepo.findById(id).orElse(null);
    if (appointment == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "找不到該筆掛號記錄"));
    }
    
    // 取得前端傳來的狀態（例如 "CANCELLED"）
    String newStatus = body.get("status");
    if (!"CANCELLED".equals(newStatus)) {
        return ResponseEntity.badRequest().body(Map.of("message", "不合法的狀態變更"));
    }
    
    // 更新狀態並存檔
    appointment.setStatus("CANCELLED");
    appointmentRepo.save(appointment);
    
    return ResponseEntity.ok(Map.of("message", "掛號已成功取消"));
    }
}