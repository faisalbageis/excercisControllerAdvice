package org.example.capstone2.Service;

import lombok.RequiredArgsConstructor;
import org.example.capstone2.APi.ApiException;
import org.example.capstone2.Model.Admin;
import org.example.capstone2.Model.Lawyer;
import org.example.capstone2.Repository.AdminRepository;
import org.example.capstone2.Repository.LawyerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final LawyerRepository lawyerRepository;
    private final EmailService emailService;

    public List<Admin> getAdmins(){
        List<Admin> admins =  adminRepository.findAll();
        if(admins.isEmpty()){
            throw new ApiException("there is no Admins in the system");
        }
        return admins;
    }

    public void addAdmin(Admin admin){
        adminRepository.save(admin);
    }

    public void updateAdmin(Integer id, Admin admin){
        Admin oldAdmin = adminRepository.findAdminById(id);

        if(oldAdmin == null){
            throw new ApiException("id not found");
        }

        oldAdmin.setFullName(admin.getFullName());
        oldAdmin.setEmail(admin.getEmail());
        oldAdmin.setPassword(admin.getPassword());

        adminRepository.save(oldAdmin);
    }

    public void deleteAdmin(Integer id){
        Admin oldAdmin = adminRepository.findAdminById(id);

        if(oldAdmin == null){
            throw new ApiException("id not found");
        }

        adminRepository.delete(oldAdmin);

    }

    public void updateLawyerStatus(Integer adminId, Integer lawyerId, String status){

        Admin admin = adminRepository.findAdminById(adminId);

        if(admin == null){
           throw new ApiException("admin not found");
        }

        Lawyer lawyer = lawyerRepository.findLawyerById(lawyerId);

        if(lawyer == null){
            throw new ApiException("lawyer not found");
        }

        if(!status.equalsIgnoreCase("Active") && !status.equalsIgnoreCase("Blocked")){
            throw new ApiException("status must be Active or Blocked");
        }

        lawyer.setStatus(status);
        lawyerRepository.save(lawyer);

        if(status.equalsIgnoreCase("Active")){
            emailService.sendEmail(lawyer.getEmail(),
                    "activate account",
                    "your Account Has Been Activated");
        }else {
            emailService.sendEmail(lawyer.getEmail(),
                    "Blocked account",
                    "your Account Has Been Blocked");
        }

    }
}