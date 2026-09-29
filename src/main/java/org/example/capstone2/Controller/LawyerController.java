package org.example.capstone2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstone2.APi.ApiException;
import org.example.capstone2.APi.ApiResponse;
import org.example.capstone2.Model.Lawyer;
import org.example.capstone2.Service.LawyerService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/Lawyer")
@RequiredArgsConstructor
public class LawyerController {

    private final LawyerService lawyerService;

    @GetMapping("/get")
    public ResponseEntity<?> getLawyers() {

        List<Lawyer> lawyers = lawyerService.getLawyers();

        return ResponseEntity.status(200).body(lawyers);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addLawyer(@RequestBody @Valid Lawyer newLawyer, Errors errors) {

        if (errors.hasErrors()) {
            String massage = errors.getFieldError().getDefaultMessage();
            throw new ApiException(massage);
        }

        lawyerService.addLawyer(newLawyer);

        return ResponseEntity.status(200).body(new ApiResponse("lawyer added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateLawyer(
            @PathVariable Integer id,
            @RequestBody @Valid Lawyer newLawyer,
            Errors errors) {

        if (errors.hasErrors()) {
            String massage = errors.getFieldError().getDefaultMessage();
            throw new ApiException(massage);
        }

        lawyerService.updateLawyer(id, newLawyer);

        return ResponseEntity.status(200).body(new ApiResponse("lawyer updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLawyer(@PathVariable Integer id) {

        lawyerService.deleteLawyer(id);

        return ResponseEntity.status(200).body(new ApiResponse("lawyer deleted successfully"));
    }

    @GetMapping("/search/city/{city}")
    public ResponseEntity<?> getLawyersByCity(@PathVariable String city) {

        List<Lawyer> lawyers = lawyerService.getLawyersByCity(city);

        return ResponseEntity.status(200).body(lawyers);
    }

    @GetMapping("/search/specialty/{specialty}")
    public ResponseEntity<?> getLawyersBySpecialty(@PathVariable String specialty) {

        List<Lawyer> lawyers = lawyerService.getLawyersBySpecialty(specialty);

        return ResponseEntity.status(200).body(lawyers);
    }

    @GetMapping("/search/price/{min}/{max}")
    public ResponseEntity<?> getLawyersByPrice(@PathVariable double min, @PathVariable double max) {

        List<Lawyer> lawyers = lawyerService.getLawyersByPrice(min, max);

        return ResponseEntity.status(200).body(lawyers);
    }

    @GetMapping("/get/active")
    public ResponseEntity<?> getActiveLawyers() {

        List<Lawyer> lawyers = lawyerService.getActiveLawyers();

        return ResponseEntity.status(200).body(lawyers);
    }

    @GetMapping("/get/pending")
    public ResponseEntity<?> getPendingLawyers() {

        List<Lawyer> lawyers = lawyerService.getPendingLawyers();

        return ResponseEntity.status(200).body(lawyers);
    }

    @GetMapping("/filter/{specialty}/{city}/{max}")
    public ResponseEntity<?> getLawyerFilter(@PathVariable String specialty, @PathVariable String city, @PathVariable double max) {

        List<Lawyer> lawyers = lawyerService.lawyerFilter(specialty, city, max);

        return ResponseEntity.status(200).body(lawyers);
    }

    @PostMapping("/login/{email}/{password}")
    public ResponseEntity<?> login(@PathVariable String email, @PathVariable String password) {

        lawyerService.login(email, password);

        return ResponseEntity.status(200).body(new ApiResponse("login successfully"));
    }
}