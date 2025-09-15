package com.example.armtek_parcer_new.aliance.repository;

import com.example.armtek_parcer_new.aliance.domain.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProducerJpaRepository extends JpaRepository<Producer, Long> {
}
