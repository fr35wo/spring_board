package com.example.soulware_week1.global.config;

import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    // 현재 데이터베이스 연결을 결정하기 위해 호출하는 메서드
    protected Object determineCurrentLookupKey() {
        // 현재 트랜잭션이 읽기 전용인지 확인
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        String dataSourceKey = isReadOnly ? "slave" : "master";

        // MDC에 데이터 소스 정보 설정
        MDC.put("datasource", dataSourceKey);

        return dataSourceKey;
    }
}
