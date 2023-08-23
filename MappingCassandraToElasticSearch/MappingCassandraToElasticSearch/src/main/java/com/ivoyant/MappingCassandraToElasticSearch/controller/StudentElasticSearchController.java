package com.ivoyant.MappingCassandraToElasticSearch.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivoyant.MappingCassandraToElasticSearch.entity.ElasticStudent;
import com.ivoyant.MappingCassandraToElasticSearch.entity.Student;
import com.ivoyant.MappingCassandraToElasticSearch.repository.StudentElasticSearchRepository;
import com.ivoyant.MappingCassandraToElasticSearch.service.CassandraStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("api/ElasticSearch/")
public class StudentElasticSearchController {
    @Autowired
   private CassandraStudentService cassandraStudentService;
   @Autowired
   private StudentElasticSearchRepository studentElasticSearchRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentElasticSearchController.class);


    /*@PostMapping("loadAll")
  public List<ElasticStudent>saveStudent() throws JsonProcessingException {
       List<ElasticStudent> student2 = (List<ElasticStudent>) cassandraStudentService.getStudents();
       for (ElasticStudent Student1:student2) {
           studentElasticSearchRepository.save(Student1);
       }
        return student2;
    }*/
    /*@PostMapping("insertMultipleRecords")
    public ResponseEntity<HttpStatus> insert(@RequestBody List<ElasticStudent> elasticStudents){
        try {
            for (ElasticStudent elasticStudent:elasticStudents){
                studentElasticSearchRepository.save(elasticStudent);
            }
        }catch (Exception e){
            LOGGER.info(e.getMessage());

        }

        return ResponseEntity.ok(HttpStatus.CREATED);
    }*/

    private final Executor asyncExecutor;

    @Autowired
    public StudentElasticSearchController(StudentElasticSearchRepository studentElasticSearchRepository, Executor asyncExecutor) {
        this.studentElasticSearchRepository = studentElasticSearchRepository;
        this.asyncExecutor = asyncExecutor;
    }

    @PostMapping("/insertMultipleRecords")
    public ResponseEntity<HttpStatus> insert(@RequestBody List<ElasticStudent> elasticStudents) {
        CompletableFuture.runAsync(() -> insertAsync(elasticStudents), asyncExecutor);
        return ResponseEntity.accepted().build();
    }

    private void insertAsync(List<ElasticStudent> elasticStudents) {
        try {
            for (ElasticStudent elasticStudent : elasticStudents) {
                studentElasticSearchRepository.save(elasticStudent);
            }
        } catch (Exception e) {
            LOGGER.error("Error inserting students asynchronously: " + e.getMessage());
        }
    }
   @PostMapping("loadAll")
   public ResponseEntity<HttpStatus> saveStudent() throws JsonProcessingException {
        try {
            List<Student> cassandraStudents = cassandraStudentService.getStudents();

            List<ElasticStudent> elasticStudents = StreamSupport.stream(cassandraStudents.spliterator(), false)
                    .map(this::convertToElasticStudent)
                    .collect(Collectors.toList());

           // List<ElasticStudent> savedStudents = new ArrayList<>();
            for (ElasticStudent elasticStudent : elasticStudents) {
                studentElasticSearchRepository.save(elasticStudent);
            }
        }catch (Exception e){
            LOGGER.info(e.getMessage());
        }

       return ResponseEntity.ok(HttpStatus.CREATED);
   }

    private ElasticStudent convertToElasticStudent(Student student) {
        ElasticStudent elasticStudent = new ElasticStudent();
        // Populate elasticStudent properties based on student properties
        elasticStudent.setId(student.getId());
        elasticStudent.setName(student.getName());
        elasticStudent.setAddress(student.getAddress());
        elasticStudent.setGrade(student.getGrade());
        elasticStudent.setSubjects(student.getSubjects());
        // ... other properties

        return elasticStudent;
    }
    @GetMapping("getAllStudents")
    public  Iterable<ElasticStudent> getAllStudents(){
       return studentElasticSearchRepository.findAll();
    }
    @GetMapping("getStudent/{uuid}")
    public Optional<ElasticStudent> getStudentById(@PathVariable UUID uuid){
       return studentElasticSearchRepository.findById(uuid);
    }


    @PutMapping("updateStudent/{uuid}")
    public ElasticStudent updateStudent(@PathVariable UUID uuid,@RequestBody ElasticStudent elasticStudent){
       ElasticStudent elasticStudent1 = studentElasticSearchRepository.findById(uuid).get();
       elasticStudent1.setName(elasticStudent.getName());
       elasticStudent1.setAddress(elasticStudent.getAddress());
       elasticStudent1.setGrade(elasticStudent.getGrade());
       elasticStudent1.setSubjects(elasticStudent.getSubjects());
       return studentElasticSearchRepository.save(elasticStudent1);
    }

    @DeleteMapping("deleteStudent/{uuid}")
    public String deleteStudent(@PathVariable UUID uuid){
       ElasticStudent elasticStudent = studentElasticSearchRepository.findById(uuid).get();
       if (elasticStudent == null){
           return "Student Not Found";
       }else {
           studentElasticSearchRepository.deleteById(uuid);
           return "Student Successfully Deleted";
       }
    }



}
