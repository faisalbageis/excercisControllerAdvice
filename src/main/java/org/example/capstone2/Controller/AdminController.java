package org.example.capstone2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstone2.APi.ApiException;
import org.example.capstone2.APi.ApiResponse;
import org.example.capstone2.Model.Admin;
import org.example.capstone2.Service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/Admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/get")
    public ResponseEntity<?> getAdmins(){
        List<Admin> admins = adminService.getAdmins();

        if(admins.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("there is no Admins in the system"));
        }

        return ResponseEntity.status(200).body(admins);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAdmin(@RequestBody @Valid Admin admin, Errors errors){
        if(errors.hasErrors()){
            String massage = errors.getFieldError().getDefaultMessage();
            throw new ApiException(massage);
        }

        adminService.addAdmin(admin);

        return ResponseEntity.status(200).body(new ApiResponse("admin added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable Integer id, @RequestBody @Valid Admin admin, Errors errors){
        if(errors.hasErrors()){
            String massage = errors.getFieldError().getDefaultMessage();
            throw new ApiException(massage);
        }

        adminService.updateAdmin(id, admin);

            return ResponseEntity.status(200).body(new ApiResponse("admin updated successfully"));


    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Integer id){
        adminService.deleteAdmin(id);


            return ResponseEntity.status(200).body(new ApiResponse("admin deleted successfully"));


    }

    @PutMapping("/lawyer/status/{adminId}/{lawyerId}/{status}")
    public ResponseEntity<?> updateLawyerStatus(@PathVariable Integer adminId, @PathVariable Integer lawyerId, @PathVariable String status){

        adminService.updateLawyerStatus(adminId, lawyerId, status);

        return ResponseEntity.status(200).body(new ApiResponse("lawyer status updated successfully"));
    }
}
