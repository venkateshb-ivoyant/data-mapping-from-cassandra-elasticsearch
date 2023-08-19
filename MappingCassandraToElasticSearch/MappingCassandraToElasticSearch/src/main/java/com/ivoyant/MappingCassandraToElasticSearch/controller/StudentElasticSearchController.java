package com.ivoyant.MappingCassandraToElasticSearch.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivoyant.MappingCassandraToElasticSearch.entity.ElasticStudent;
import com.ivoyant.MappingCassandraToElasticSearch.entity.Student;
import com.ivoyant.MappingCassandraToElasticSearch.repository.StudentElasticSearchRepository;
import com.ivoyant.MappingCassandraToElasticSearch.service.CassandraStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("api/ElasticSearch/")
public class StudentElasticSearchController {
    @Autowired
   private CassandraStudentService cassandraStudentService;
   @Autowired
   private StudentElasticSearchRepository studentElasticSearchRepository;

   /*@PostMapping("loadAll")
  public List<ElasticStudent>saveStudent() throws JsonProcessingException {
       List<ElasticStudent> student2 = (List<ElasticStudent>) cassandraStudentService.getStudents();
       for (ElasticStudent Student1:student2) {
           studentElasticSearchRepository.save(Student1);
       }
        return student2;
    }*/
   @PostMapping("loadAll")
   public List<ElasticStudent> saveStudent() throws JsonProcessingException {
       List<Student> cassandraStudents = cassandraStudentService.getStudents();

       List<ElasticStudent> elasticStudents = StreamSupport.stream(cassandraStudents.spliterator(), false)
               .map(this::convertToElasticStudent)
               .collect(Collectors.toList());

       List<ElasticStudent> savedStudents = new ArrayList<>();
       for (ElasticStudent elasticStudent : elasticStudents) {
           savedStudents.add(studentElasticSearchRepository.save(elasticStudent));
       }

       return savedStudents;
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
