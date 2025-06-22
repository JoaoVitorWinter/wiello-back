package com.wiello_back.repository;

import com.wiello_back.entity.Project;
import com.wiello_back.entity.WielloUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findAllByOwnerOrderByCreationDateDesc(WielloUser wielloUser);
}
