package com.example.armtek_parcer_new.aliance.repository;

import com.example.armtek_parcer_new.aliance.domain.GoodUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GoodUrlRepositoryJpa extends JpaRepository<GoodUrl, Long> {
    @Query("SELECT COUNT (g.url) FROM GoodUrl g WHERE g.producerClean = :producerClean")
    int findUrlsByProducerClean(@Param("producerClean") String producerClean);

}