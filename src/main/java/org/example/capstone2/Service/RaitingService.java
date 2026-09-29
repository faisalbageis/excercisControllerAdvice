package org.example.capstone2.Service;

import lombok.RequiredArgsConstructor;
import org.example.capstone2.APi.ApiException;
import org.example.capstone2.Model.Case;
import org.example.capstone2.Model.Lawyer;
import org.example.capstone2.Model.Raiting;
import org.example.capstone2.Model.Review;
import org.example.capstone2.Repository.CaseRepository;
import org.example.capstone2.Repository.LawyerRepository;
import org.example.capstone2.Repository.RaitingRepository;
import org.example.capstone2.Repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RaitingService {

    private final RaitingRepository raitingRepository;
    private final LawyerRepository lawyerRepository;
    private final ReviewRepository reviewRepository;
    private final CaseRepository caseRepository;

    public List<Raiting> getRaitings(){
        List<Raiting> raitings = raitingRepository.findAll();

        if(raitings.isEmpty()){
            throw new ApiException("there is no Raitings in the system");
        }

        return raitings;
    }

    public void addRaiting(Raiting raiting){

        Lawyer lawyer = lawyerRepository.findLawyerById(raiting.getLawyerId());

        if(lawyer == null){
            throw new ApiException("lawyer id not found");
        }

        double sum=0.0;
        int count=0;

        List<Case> cases = caseRepository.findCaseByLawyerId(raiting.getLawyerId());

        for(Case i:cases){
            Review review = reviewRepository.findReviewByCaseId(i.getId());
            if(review!=null){
                sum +=review.getRating();
                count++;
            }
        }

        if(count==0){
            throw new ApiException("lawyer dont have cases");
        }

        double raite = sum/count;
        raiting.setRating(raite);
        raitingRepository.save(raiting);
    }

    public void deleteRaiting(Integer id){

        Raiting oldRaiting = raitingRepository.findRaitingById(id);

        if(oldRaiting == null){
            throw new ApiException("id not found");
        }

        raitingRepository.delete(oldRaiting);

    }

    public Raiting getLawyerRaiting(Integer lawyerId){
        Raiting raiting = raitingRepository.findRaitingByLawyerId(lawyerId);

        if(raiting==null){
            throw new ApiException("lawyer dont have rating");
        }

        return raiting;
    }

    public void updateRaiting(Integer lawyerId){
        Raiting oldRaiting = raitingRepository.findRaitingByLawyerId(lawyerId);

        if(oldRaiting == null){
            throw new ApiException("there is no rating for this lawyer");
        }

        List<Case> cases = caseRepository.findCaseByLawyerId(lawyerId);

        double sum = 0.0;
        int count = 0;

        for(Case i : cases){

            Review review = reviewRepository.findReviewByCaseId(i.getId());

            if(review != null){
                sum += review.getRating();
                count++;
            }
        }

        if(count == 0){
            throw new ApiException("lawyer dont have cases");
        }

        double raite = sum / count;

        oldRaiting.setRating(raite);

        raitingRepository.save(oldRaiting);

    }

}