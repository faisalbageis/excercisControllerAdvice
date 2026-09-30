package org.example.capstone2.Service;

import lombok.RequiredArgsConstructor;
import org.example.capstone2.APi.ApiException;
import org.example.capstone2.Model.Case;
import org.example.capstone2.Model.Lawyer;
import org.example.capstone2.Model.Request;
import org.example.capstone2.Model.User;
import org.example.capstone2.Repository.LawyerRepository;
import org.example.capstone2.Repository.RequestRepository;
import org.example.capstone2.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final LawyerRepository lawyerRepository;
    private final UserRepository userRepository;
    private final CaseService caseService;
    private final EmailService emailService;

    public List<Request> getRequests(){
        List<Request> requests = requestRepository.findAll();

        if(requests.isEmpty()){
            throw new ApiException("there is no Requests in the system");
        }

        return requests;
    }

    public void addRequest(Request request){
        User user = userRepository.findUserById(request.getUserId());
        if(user==null){
            throw new ApiException("user id not found");
        }

        Lawyer lawyer = lawyerRepository.findLawyerById(request.getLawyerId());
        if(lawyer==null){
            throw new ApiException("lawyer id not found");
        }

        if(!lawyer.getStatus().equalsIgnoreCase("active")) {
            throw new ApiException("lawyer is not active");
        }

        request.setStatus("Pending");
        requestRepository.save(request);
    }

    public void updateRequest(Integer id,Request request){

        User user = userRepository.findUserById(request.getUserId());
        if(user==null){
            throw new ApiException("user id not found");
        }

        Lawyer lawyer = lawyerRepository.findLawyerById(request.getLawyerId());
        if(lawyer==null){
            throw new ApiException("lawyer id not found");
        }

        Request oldRequest = requestRepository.findRequestById(id);

        if(oldRequest == null){
            throw new ApiException("request id not found");
        }

        oldRequest.setCaseType(request.getCaseType());
        oldRequest.setDescription(request.getDescription());
        oldRequest.setLawyerId(request.getLawyerId());
        oldRequest.setUserId(request.getUserId());
        requestRepository.save(oldRequest);
    }

    public void deleteRequest(Integer id){
        Request oldRequest = requestRepository.findRequestById(id);

        if(oldRequest == null){
            throw new ApiException("id not found");
        }

        requestRepository.delete(oldRequest);
    }

    public List<Request> getLawyerRequests(Integer lawyerId){
        List<Request> requests = requestRepository.findRequestByLawyerId(lawyerId);

        if(requests.isEmpty()){
            throw new ApiException("you dont have requests");
        }

        return requests;
    }

    public List<Request> getUserRequests(Integer UserId){
        List<Request> requests = requestRepository.findRequestByUserId(UserId);

        if(requests.isEmpty()){
            throw new ApiException("you dont have requests");
        }

        return requests;
    }

    public void acceptRequest(Integer RequestID,Integer lawyerID){
        Request request = requestRepository.findRequestById(RequestID);

        if(request ==null){
            throw new ApiException("request id not found");
        }

        if(!request.getStatus().equalsIgnoreCase("pending")) {
            throw new ApiException("request is not pending");
        }

        if(!request.getLawyerId().equals(lawyerID)) {
            throw new ApiException("lawyer id dont match");
        }

        request.setStatus("Accepted");
        requestRepository.save(request);

        Case newcase = new Case();
        newcase.setRequestId(request.getId());
        newcase.setLawyerId(request.getLawyerId());
        newcase.setUserId(request.getUserId());
        newcase.setCaseType(request.getCaseType());
        newcase.setDescription(request.getDescription());

        caseService.addCase(newcase);

        User user = userRepository.findUserById(request.getUserId());
        emailService.sendEmail(user.getEmail(),
                "Request Accepted",
                "Your request has been accepted by the lawyer.");
    }


    public void rejectRequest(Integer requestId,Integer LawyerId){

        Request request = requestRepository.findRequestById(requestId);

        if(request==null){
            throw new ApiException("request id not found");
        }

        if(!request.getStatus().equalsIgnoreCase("pending")) {
            throw new ApiException("request is not pending");
        }

        if(!request.getLawyerId().equals(LawyerId)){
            throw new ApiException("lawyer id do not match");
        }

        request.setStatus("Rejected");
        requestRepository.save(request);

        User user = userRepository.findUserById(request.getUserId());
        emailService.sendEmail(user.getEmail(),
                "Request Rejected",
                "Your request has been rejected by the lawyer.");
    }


    public void cancelRequest(Integer requestId,Integer userID){
        Request request = requestRepository.findRequestById(requestId);

        if(request==null){
            throw new ApiException("request id not found");
        }

        if(!request.getUserId().equals(userID)){
            throw new ApiException("user id do not match");
        }

        if(!request.getStatus().equals("pending")){
            throw new ApiException("request is not pending ");
        }

        request.setStatus("Cancelled");
        requestRepository.save(request);

        User user = userRepository.findUserById(userID);
        emailService.sendEmail(user.getEmail(),
                "Request Cancelled",
                "Your consultation request has been cancelled successfully.");
    }
}