package org.example.capstone2.Service;

import lombok.RequiredArgsConstructor;
import org.example.capstone2.APi.ApiException;
import org.example.capstone2.Model.Lawyer;
import org.example.capstone2.Repository.LawyerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LawyerService {

    private final LawyerRepository lawyerRepository;

    public List<Lawyer> getLawyers() {
        List<Lawyer> lawyers = lawyerRepository.findAll();

        if (lawyers.isEmpty()) {
            throw new ApiException("there is no Lawyers in the system");
        }

        return lawyers;
    }

    public void addLawyer(Lawyer lawyer) {

        lawyer.setStatus("pended");

        lawyerRepository.save(lawyer);
    }

    public void updateLawyer(Integer id, Lawyer lawyer) {

        Lawyer oldLawyer = lawyerRepository.findLawyerById(id);

        if (oldLawyer == null) {
            throw new ApiException("id not found");
        }

        oldLawyer.setEmail(lawyer.getEmail());
        oldLawyer.setFullName(lawyer.getFullName());
        oldLawyer.setPhoneNumber(lawyer.getPhoneNumber());
        oldLawyer.setPassword(lawyer.getPassword());
        oldLawyer.setCity(lawyer.getCity());
        oldLawyer.setConsultationPrice(lawyer.getConsultationPrice());
        oldLawyer.setLicenseNumber(lawyer.getLicenseNumber());
        oldLawyer.setSpecialty(lawyer.getSpecialty());

        lawyerRepository.save(oldLawyer);
    }

    public void deleteLawyer(Integer id) {

        Lawyer oldLawyer = lawyerRepository.findLawyerById(id);

        if (oldLawyer == null) {
            throw new ApiException("id not found");
        }

        lawyerRepository.delete(oldLawyer);
    }

    public List<Lawyer> getLawyersByCity(String city) {

        List<Lawyer> lawyers =
                lawyerRepository.findLawyerByCityAndStatus(city, "active");

        if (lawyers.isEmpty()) {
            throw new ApiException("there is no lawyers in this city");
        }

        return lawyers;
    }

    public List<Lawyer> getLawyersBySpecialty(String specialty) {

        List<Lawyer> lawyers = lawyerRepository.findLawyerBySpecialtyAndStatus(specialty, "active");

        if (lawyers.isEmpty()) {
            throw new ApiException("there is no lawyers in this Specialty");
        }

        return lawyers;
    }

    public List<Lawyer> getLawyersByPrice(double min, double max) {

        List<Lawyer> lawyers = lawyerRepository.findLawyerByConsultationPriceBetweenAndStatus(min, max, "active");

        if (lawyers.isEmpty()) {
            throw new ApiException("there is no lawyers in this range");
        }

        return lawyers;
    }

    public List<Lawyer> getActiveLawyers() {

        List<Lawyer> lawyers = lawyerRepository.findLawyerByStatus("active");

        if (lawyers.isEmpty()) {
            throw new ApiException("there is no active lawyers");
        }

        return lawyers;
    }

    public List<Lawyer> getPendingLawyers() {

        List<Lawyer> lawyers = lawyerRepository.findLawyerByStatus("pended");

        if (lawyers.isEmpty()) {
            throw new ApiException("there is no pending lawyers");
        }

        return lawyers;
    }

    public List<Lawyer> lawyerFilter(String specialty, String city, double max) {

        List<Lawyer> lawyers = lawyerRepository.lawyerFilter(specialty, city, max);

        if (lawyers.isEmpty()) {
            throw new ApiException("there is no lawyers found");
        }

        return lawyers;
    }

    public void login(String email, String password) {

        Lawyer lawyer = lawyerRepository.findLawyerByEmail(email);

        if (lawyer == null) {
            throw new ApiException("wrong email");
        }

        if (!lawyer.getPassword().equals(password)) {
            throw new ApiException("wrong password");
        }

    }
}