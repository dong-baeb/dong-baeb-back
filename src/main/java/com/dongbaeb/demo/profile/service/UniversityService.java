package com.dongbaeb.demo.profile.service;

import com.dongbaeb.demo.entity.University;
import com.dongbaeb.demo.profile.dto.UniversityRequest;
import com.dongbaeb.demo.profile.dto.UniversityResponse;
import com.dongbaeb.demo.profile.repository.UniversityRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UniversityService {
    private final UniversityRepository universityRepository;

    public UniversityService(UniversityRepository universityRepository) {
        this.universityRepository = universityRepository;
    }

    @Transactional
    public UniversityResponse createUniversity(UniversityRequest universityRequest) {
        University university = new University();
        university.setName(universityRequest.name());
        universityRepository.save(university);

        return new UniversityResponse(university.getId(), university.getName());
    }

}
