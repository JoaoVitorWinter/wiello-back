package com.wiello_back.repository;

import com.wiello_back.entity.ProjectColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProjectColumnRepository extends JpaRepository<ProjectColumn, UUID> {
}
