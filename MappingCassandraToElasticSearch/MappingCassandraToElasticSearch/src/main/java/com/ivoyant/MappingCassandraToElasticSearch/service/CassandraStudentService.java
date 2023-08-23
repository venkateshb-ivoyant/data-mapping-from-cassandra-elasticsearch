package com.ivoyant.MappingCassandraToElasticSearch.service;

import com.ivoyant.MappingCassandraToElasticSearch.entity.Student;
import com.ivoyant.MappingCassandraToElasticSearch.repository.StudentCassandraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class CassandraStudentService {
    @Autowired
    private StudentCassandraRepository studentCassandraRepository;
    public Student saveStudent(Student student){
        return studentCassandraRepository.save(student);
    }
    public List<Student> getStudents(){
        return studentCassandraRepository.findAll();
    }
    public Student updateStudent(Student student, String id){
        var existingStudent = studentCassandraRepository.findById(UUID.fromString(id));
        if(!existingStudent.isPresent())
            throw new RuntimeException("Student not found!!");
        student.setId(UUID.fromString(id));
        return studentCassandraRepository.save(student);
    }
    public Student getStudentByID(String id){

        return studentCassandraRepository.findById(UUID.fromString(id)).get();
    }
    public String deleteStudent(String id){
        studentCassandraRepository.deleteById(UUID.fromString(id));
        return "Student deleted successfully";
    }
}

