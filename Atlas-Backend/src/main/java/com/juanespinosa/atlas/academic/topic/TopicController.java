package com.juanespinosa.atlas.academic.topic;

import com.juanespinosa.atlas.academic.subject.Subject;
import com.juanespinosa.atlas.academic.subject.SubjectRepository;
import com.juanespinosa.atlas.exception.ResourceNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/topics")
public class TopicController {

    private final TopicRepository topicRepository;
    private final SubjectRepository subjectRepository;

    public TopicController(TopicRepository topicRepository, SubjectRepository subjectRepository) {
        this.topicRepository = topicRepository;
        this.subjectRepository = subjectRepository;
    }

    @GetMapping("/{id}")
    public Topic getById(@PathVariable Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));
    }


    // GET /topics?subjectId=5 --- topic of subject with id = 5
    @GetMapping
    public List<Topic> getBySubject(@RequestParam Long subjectId) {
        return topicRepository.findBySubjectId(subjectId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTopic(@PathVariable long id){
        topicRepository.deleteById(id);
    }

    /*
     * A Topic creation request is sent, containing title, content and a subjectId.
     * We look up the existing Subject by that id to link the new Topic to it.
     * Then we build the Topic with title, content and that subject reference,
     * and persist it.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Topic create(@RequestBody TopicCreateRequest request) {
        Subject subject = subjectRepository.findById(request.subjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        Topic topic = new Topic();
        topic.setTitle(request.title());
        topic.setContent(request.content());
        topic.setSubject(subject);

        return topicRepository.save(topic);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Topic editTopic(@PathVariable Long id, @RequestBody TopicCreateRequest request) {
        Topic existing = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

        Subject subject = subjectRepository.findById(request.subjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        existing.setTitle(request.title());
        existing.setContent(request.content());
        existing.setSubject(subject);

        return topicRepository.save(existing);
    }


}