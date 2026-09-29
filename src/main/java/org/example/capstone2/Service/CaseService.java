package org.example.capstone2.Service;

import lombok.RequiredArgsConstructor;
import org.example.capstone2.APi.ApiException;
import org.example.capstone2.Model.Case;
import org.example.capstone2.Model.Request;
import org.example.capstone2.Model.Lawyer;
import org.example.capstone2.Model.User;
import org.example.capstone2.Repository.CaseRepository;
import org.example.capstone2.Repository.RequestRepository;
import org.example.capstone2.Repository.LawyerRepository;
import org.example.capstone2.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CaseService {

    private final CaseRepository caseRepository;
    private final RequestRepository requestRepository;
    private final LawyerRepository lawyerRepository;
    private final UserRepository userRepository;
    private  final EmailService emailService;

    public List<Case> getCases() {
        List<Case> cases= caseRepository.findAll();

        if(cases.isEmpty()){
            throw new ApiException("there is no Cases in the system");
        }
        return cases;
    }

    public void addCase(Case caseObject) {

        Request request = requestRepository.findRequestById(caseObject.getRequestId());

        if (request == null) {
            throw new ApiException("request id not found");
        }

        User user = userRepository.findUserById(caseObject.getUserId());

        if (user == null) {
            throw new ApiException("user id not found");
        }

        Lawyer lawyer = lawyerRepository.findLawyerById(caseObject.getLawyerId());

        if (lawyer == null) {
            throw new ApiException("lawyer id not found");
        }

        caseRepository.save(caseObject);
    }

    public void updateCase(Integer id, Case caseObject) {

        Request request = requestRepository.findRequestById(caseObject.getRequestId());

        if (request == null) {
            throw new ApiException("request id not found");
        }

        User user = userRepository.findUserById(caseObject.getUserId());

        if (user == null) {
            throw new ApiException("user id not found");
        }

        Lawyer lawyer = lawyerRepository.findLawyerById(caseObject.getLawyerId());

        if (lawyer == null) {
            throw new ApiException("lawyer id not found");
        }

        Case oldCase = caseRepository.findCaseById(id);

        if (oldCase == null) {
            throw new ApiException("case id not found");
        }

        oldCase.setRequestId(caseObject.getRequestId());
        oldCase.setUserId(caseObject.getUserId());
        oldCase.setLawyerId(caseObject.getLawyerId());
        oldCase.setCaseType(caseObject.getCaseType());
        oldCase.setDescription(caseObject.getDescription());

        caseRepository.save(oldCase);
    }

    public void deleteCase(Integer id) {

        Case oldCase = caseRepository.findCaseById(id);

        if (oldCase == null) {
            throw new ApiException("id not found");
        }

        caseRepository.delete(oldCase);

    }


    public Case getCaseByID(Integer id){
        Case found = caseRepository.findCaseById(id);
        if(found==null){
            throw new ApiException("there is no case with this id");
        }
        return found;
    }


    public List<Case> getCasesByLawyerId(Integer lawyerId){
        List<Case> found = caseRepository.findCaseByLawyerId(lawyerId);
        if(found.isEmpty()){
            throw new ApiException("you dont have casses");
        }
        return found;
    }

    public List<Case> getCasesByUserId(Integer userId){
        List<Case> found = caseRepository.findCaseByUserId(userId);
        if(found.isEmpty()){
            throw new ApiException("you dont have cases");
        }
        return found;
    }

    public void closeCase(Integer CaseId,Integer lawyerId){
        Case foundCase = caseRepository.findCaseById(CaseId);
        if(foundCase== null){
            throw new ApiException("case is not found");
        }

        if(foundCase.getStatus().equalsIgnoreCase("closed")){
            throw new ApiException("case is closed");
        }

        if(!foundCase.getLawyerId().equals(lawyerId)) {
            throw new ApiException("lawyer id do not match");
        }

        foundCase.setStatus("Closed");
        caseRepository.save(foundCase);

        User user = userRepository.findUserById(foundCase.getUserId());
        emailService.sendEmail(user.getEmail(),
                "Case Closed",
                "Your case has been closed by the lawyer.");


    }
}