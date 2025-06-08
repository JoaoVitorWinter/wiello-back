package com.wiello_back;

import com.wiello_back.entity.WielloUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WielloUserRepository extends JpaRepository<WielloUser, UUID> {
}
