package com.juanespinosa.atlas.academic.subject;

import com.juanespinosa.atlas.exception.ResourceNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectRepository subjectRepository;

    public SubjectController(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @GetMapping
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @GetMapping("/{id}")
    public Subject getById(@PathVariable long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Subject createSubject(@RequestBody SubjectCreateRequest request){

        Subject subject = new Subject();

        subject.setCode(request.code());
        subject.setName(request.name());

        return subjectRepository.save(subject);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteSubject(@PathVariable long id){
        subjectRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public  Subject editSubject(@PathVariable long id, @RequestBody Subject newSubject){
        Subject existing = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        existing.setName(newSubject.getName());
        existing.setCode(newSubject.getCode());

        return subjectRepository.save(existing);
    }

}