package com.william.alumnos.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.william.alumnos.model.Alumno;
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

}
