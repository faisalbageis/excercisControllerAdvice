package org.example.capstone2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstone2.APi.ApiException;
import org.example.capstone2.APi.ApiResponse;
import org.example.capstone2.Model.Raiting;
import org.example.capstone2.Service.RaitingService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/Raiting")
@RequiredArgsConstructor
public class RaitingController {

    private final RaitingService raitingService;

    @GetMapping("/get")
    public ResponseEntity<?> getRaitings(){

        List<Raiting> raitings = raitingService.getRaitings();

        return ResponseEntity.status(200).body(raitings);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addRaiting(@RequestBody @Valid Raiting raiting, Errors errors){

        if(errors.hasErrors()){
            String massage = errors.getFieldError().getDefaultMessage();
            throw new ApiException(massage);
        }

        raitingService.addRaiting(raiting);

        return ResponseEntity.status(200).body(new ApiResponse("raiting added successfully"));
    }

    @PutMapping("/update/{lawyerId}")
    public ResponseEntity<?> updateRaiting(@PathVariable Integer lawyerId){

        raitingService.updateRaiting(lawyerId);

        return ResponseEntity.status(200).body(new ApiResponse("rating updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRaiting(@PathVariable Integer id){

        raitingService.deleteRaiting(id);

        return ResponseEntity.status(200).body(new ApiResponse("raiting deleted successfully"));
    }

    @GetMapping("/search/{lawyerId}")
    public ResponseEntity<?> getlawyerRaiting(@PathVariable Integer lawyerId){

        Raiting raiting = raitingService.getLawyerRaiting(lawyerId);

        return ResponseEntity.status(200).body(raiting);
    }

}