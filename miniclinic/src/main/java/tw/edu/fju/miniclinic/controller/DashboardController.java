package tw.edu.fju.miniclinic.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tw.edu.fju.miniclinic.model.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Controller
public class DashboardController {

    private final DoctorRepository doctorRepo;
    private final AppointmentRepository appointmentRepo;

    public DashboardController(DoctorRepository doctorRepo, AppointmentRepository appointmentRepo) {
        this.doctorRepo = doctorRepo;
        this.appointmentRepo = appointmentRepo;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String doctorId = (String) session.getAttribute("loggedInDoctorId");
        if (doctorId == null) {
            return "redirect:/login";
        }
        
        Doctor doctor = doctorRepo.findById(doctorId).orElse(null);

        if (doctor == null) {
            session.invalidate();
            return "redirect:/login";
        }

        // 確保查詢今日掛號時，時區與病患端掛號時一致
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Taipei"));
        List<Appointment> myAppointments = appointmentRepo.findByDoctorAndApptDate(doctor, today);
        
        model.addAttribute("doctor", doctor);
        model.addAttribute("appointments", myAppointments);
        model.addAttribute("today", today);
        return "dashboard";
    }

    @GetMapping("/password")
    public String passwordForm(Model model) {
        model.addAttribute("passwordForm", new PasswordForm());
        return "password-change";
    }

    @PostMapping("/password")
    public String updatePassword(
            @Valid @ModelAttribute("passwordForm") PasswordForm form,
            BindingResult result,
            HttpSession session,
            Model model) {

        if (result.hasErrors()) return "password-change";

        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.passwordForm", "新密碼與確認密碼不符");
            return "password-change";
        }

        String doctorId = (String) session.getAttribute("loggedInDoctorId");
        if (doctorId == null) return "redirect:/login";

        Doctor doctor = doctorRepo.findById(doctorId).orElse(null);
        if (doctor == null || doctor.getPasswordHash() == null) {
            return "redirect:/login";
        }

        if (!BCrypt.checkpw(form.getOldPassword(), doctor.getPasswordHash())) {
            result.rejectValue("oldPassword", "error.passwordForm", "舊密碼錯誤");
            return "password-change";
        }

        doctor.setPasswordHash(BCrypt.hashpw(form.getNewPassword(), BCrypt.gensalt()));
        doctorRepo.save(doctor);
        model.addAttribute("successMessage", "密碼修改成功！");
        return "password-change";
    }
}