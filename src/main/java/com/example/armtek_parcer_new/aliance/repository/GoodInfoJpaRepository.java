package com.example.armtek_parcer_new.aliance.repository;

import com.example.armtek_parcer_new.aliance.domain.AlianceGoodInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodInfoJpaRepository extends JpaRepository<AlianceGoodInfo, Long> {
}
