package org.example.capstone2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstone2.APi.ApiException;
import org.example.capstone2.APi.ApiResponse;
import org.example.capstone2.Model.Request;
import org.example.capstone2.Service.RequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/Request")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @GetMapping("/get")
    public ResponseEntity<?> getRequests(){
        List<Request> requests = requestService.getRequests();

        return ResponseEntity.status(200).body(requests);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addRequest(@RequestBody @Valid Request request , Errors errors){
        if(errors.hasErrors()){
            String massage = errors.getFieldError().getDefaultMessage();
            throw new ApiException(massage);
        }

        requestService.addRequest(request);

        return ResponseEntity.status(200).body(new ApiResponse("request added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRequest(@PathVariable Integer id, @RequestBody @Valid Request request , Errors errors){
        if(errors.hasErrors()){
            String massage = errors.getFieldError().getDefaultMessage();
            throw new ApiException(massage);
        }

        requestService.updateRequest(id, request);

        return ResponseEntity.status(200).body(new ApiResponse("Request updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLawyer(@PathVariable Integer id){
        requestService.deleteRequest(id);

        return ResponseEntity.status(200).body(new ApiResponse("request deleted successfully"));
    }

    @GetMapping("/search/lawyer/{lawyerId}")
    public ResponseEntity<?> getRequestsByLawyerId(@PathVariable Integer lawyerId){
        List<Request> requests = requestService.getLawyerRequests(lawyerId);

        return ResponseEntity.status(200).body(requests);
    }


    @GetMapping("/search/user/{UserId}")
    public ResponseEntity<?> getRequestsByUserID(@PathVariable Integer UserId){
        List<Request> requests = requestService.getUserRequests(UserId);

        return ResponseEntity.status(200).body(requests);
    }

    @PutMapping("/accept/{RequestId}/{lawyerId}")
    public ResponseEntity<?> acceptRequest(@PathVariable Integer RequestId , @PathVariable Integer lawyerId){
        requestService.acceptRequest(RequestId,lawyerId);

        return ResponseEntity.status(200).body(new ApiResponse("request accepted successfully"));
    }

    @PutMapping("/reject/{requestId}/{lawyerId}")
    public ResponseEntity<?> rejectRequest(@PathVariable Integer requestId,@PathVariable Integer lawyerId){
        requestService.rejectRequest(requestId,lawyerId);

        return ResponseEntity.status(200).body(new ApiResponse("request rejected successfully"));
    }

    @PutMapping("/cancel/{requestId}/{userId}")
    public ResponseEntity<?> cancelRequest(@PathVariable Integer requestId,@PathVariable Integer userId){
        requestService.cancelRequest(requestId, userId);

        return ResponseEntity.status(200).body(new ApiResponse("request cancelled successfully"));
    }
}