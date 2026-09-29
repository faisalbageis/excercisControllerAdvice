package org.example.capstone2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstone2.APi.ApiException;
import org.example.capstone2.APi.ApiResponse;
import org.example.capstone2.Model.Case;
import org.example.capstone2.Service.CaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/Case")
@RequiredArgsConstructor
public class CaseController {

    private final CaseService caseService;

    @GetMapping("/get")
    public ResponseEntity<?> getCases(){
        List<Case> cases = caseService.getCases();



        return ResponseEntity.status(200).body(cases);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCase(@RequestBody @Valid Case newCase, Errors errors){
        if(errors.hasErrors()){
            String massage = errors.getFieldError().getDefaultMessage();
            throw new ApiException(massage);
        }

       caseService.addCase(newCase);


        return ResponseEntity.status(200).body(new ApiResponse("case added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCase(@PathVariable Integer id, @RequestBody @Valid Case newCase, Errors errors){
        if(errors.hasErrors()){
            String massage = errors.getFieldError().getDefaultMessage();
            throw new ApiException(massage);
        }

        caseService.updateCase(id, newCase);

        return ResponseEntity.status(200).body(new ApiResponse("case updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCase(@PathVariable Integer id){
        caseService.deleteCase(id);

            return ResponseEntity.status(200).body(new ApiResponse("case deleted successfully"));
    }
    @GetMapping("/search/id/{id}")
    public ResponseEntity<?> getCaseById(@PathVariable Integer id){
        Case foundCase = caseService.getCaseByID(id);
        return ResponseEntity.status(200).body(foundCase);
    }

    @GetMapping("/search/lawyer/{lawyerId}")
    public ResponseEntity<?> getLawyerCases(@PathVariable Integer lawyerId){
        List<Case> found = caseService.getCasesByLawyerId(lawyerId);
        return ResponseEntity.status(200).body(found);
    }

    @GetMapping("/search/user/{UserId}")
    public ResponseEntity<?> getUserCases(@PathVariable Integer UserId){
        List<Case> found = caseService.getCasesByUserId(UserId);
        return ResponseEntity.status(200).body(found);
    }

    @PutMapping("/close/{CaseId}/{lawyerId}")
    public ResponseEntity<?> closeCase(@PathVariable Integer CaseId , @PathVariable Integer lawyerId){
        caseService.closeCase(CaseId, lawyerId);
            return ResponseEntity.status(200).body(new ApiResponse("case closed successfully"));

    }
}
