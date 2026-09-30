package org.example.capstone2.Service;

import lombok.RequiredArgsConstructor;
import org.example.capstone2.APi.ApiException;
import org.example.capstone2.Model.Case;
import org.example.capstone2.Model.Lawyer;
import org.example.capstone2.Model.Raiting;
import org.example.capstone2.Model.Review;
import org.example.capstone2.Repository.CaseRepository;
import org.example.capstone2.Repository.LawyerRepository;
import org.example.capstone2.Repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final CaseRepository caseRepository;
    private final RaitingService raitingService;
    private final EmailService emailService;
    private final LawyerRepository lawyerRepository;

    public List<Review> getReviews(){
        List<Review> reviews = reviewRepository.findAll();

        if(reviews.isEmpty()){
            throw new ApiException("there is no Reviews in the system");
        }

        return reviews;
    }

    public void addReview(Review review){

        Case oldCase = caseRepository.findCaseById(review.getCaseId());

        if(oldCase == null){
            throw new ApiException("case id not found");
        }

        if(!oldCase.getStatus().equalsIgnoreCase("closed")){
            throw new ApiException("the case is not closed ");
        }

        Review review1 = reviewRepository.findReviewByCaseId(review.getCaseId());

        if(review1 != null){
            throw new ApiException("this case already have review");
        }

        reviewRepository.save(review);

        Lawyer lawyer = lawyerRepository.findLawyerById(oldCase.getLawyerId());

        emailService.sendEmail(lawyer.getEmail(),
                "New Review Received",
                "A user has submitted a new review for your case.");

        Raiting raiting = raitingService.getLawyerRaiting(oldCase.getLawyerId());

        if(raiting==null){
            Raiting r=new Raiting();
            r.setLawyerId(oldCase.getLawyerId());
            raitingService.addRaiting(r);
        }else {
            raitingService.updateRaiting(raiting.getLawyerId());
        }
    }

    public void updateReview(Integer id, Review review){

        Case oldCase = caseRepository.findCaseById(review.getCaseId());

        if(oldCase == null){
            throw new ApiException("case id not found");
        }

        Review oldReview = reviewRepository.findReviewById(id);

        if(oldReview == null){
            throw new ApiException("review id not found");
        }

        if(!oldReview.getCaseId().equals(oldCase.getId())){
            throw new ApiException("case id not match ");
        }

        oldReview.setRating(review.getRating());
        oldReview.setComment(review.getComment());

        reviewRepository.save(oldReview);

        Lawyer lawyer = lawyerRepository.findLawyerById(oldCase.getLawyerId());

        emailService.sendEmail(lawyer.getEmail(),
                "Review Updated",
                "A user has updated their review for your case.");

        Raiting raiting = raitingService.getLawyerRaiting(oldCase.getLawyerId());

        if(raiting==null){
            Raiting r=new Raiting();
            r.setLawyerId(oldCase.getLawyerId());
            raitingService.addRaiting(r);
        }else {
            raitingService.updateRaiting(raiting.getLawyerId());
        }
    }

    public void deleteReview(Integer id){

        Review oldReview = reviewRepository.findReviewById(id);

        if(oldReview == null){
            throw new ApiException("id not found");
        }

        reviewRepository.delete(oldReview);
    }

    public Review getCaseReview(Integer CaseID){
        Review review = reviewRepository.findReviewByCaseId(CaseID);

        if(review==null){
            throw new ApiException("this case dont have review");
        }

        return review;
    }
}