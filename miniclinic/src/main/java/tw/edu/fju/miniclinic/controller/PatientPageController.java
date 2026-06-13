package tw.edu.fju.miniclinic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import tw.edu.fju.miniclinic.model.Patient;
import tw.edu.fju.miniclinic.model.PatientRepository;

import java.util.List;
import java.util.Optional;

@Controller
public class PatientPageController {

    private final PatientRepository patientRepo;

    public PatientPageController(PatientRepository patientRepo) {
        this.patientRepo = patientRepo;
    }

    @GetMapping("/patients")
    public String listPatients(Model model) {
        List<Patient> patients = patientRepo.findAll();
        model.addAttribute("patients", patients);
        
        return "patients"; 
    }

    @GetMapping("/patients/{chartNo}")
    public String patientDetail(@PathVariable String chartNo, Model model) {
        Optional<Patient> patient = patientRepo.findById(chartNo);

        if (patient.isEmpty()) {
            return "redirect:/patients";
        }

        model.addAttribute("patient", patient.get());
        return "patient-detail";
    }
}