package com.example.armtek_parcer_new.aliance.repository;

import com.example.armtek_parcer_new.aliance.domain.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerJpaRepository extends JpaRepository<Owner, Long> {
}
