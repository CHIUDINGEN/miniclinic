package tw.edu.fju.miniclinic.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import tw.edu.fju.miniclinic.model.Patient;
import tw.edu.fju.miniclinic.model.PatientRepository;

import java.util.List;
import java.util.Optional;

@RestController
public class PatientApiController {

    private final PatientRepository patientRepo;

    public PatientApiController(PatientRepository patientRepo) {
        this.patientRepo = patientRepo;
    }

    @GetMapping("/api/patients")
    public List<Patient> getPatients() {
        return patientRepo.findAll();
    }

    @GetMapping("/api/patients/{chartNo}")
    public ResponseEntity<Patient> getPatient(@PathVariable String chartNo) {
        Optional<Patient> patient = patientRepo.findById(chartNo);
        
        return patient
            .map(p -> ResponseEntity.ok(p))            
            .orElse(ResponseEntity.notFound().build()); 
    }
}