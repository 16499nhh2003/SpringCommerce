package com.project.spring.repositories;

import com.project.spring.model.Manufacture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ManufactureRepository extends JpaRepository<Manufacture,Long> {
     List<Manufacture> findManufactureById(Long id);
     Optional<Manufacture> findManufactureByNameContainsIgnoreCase(String name);
}
