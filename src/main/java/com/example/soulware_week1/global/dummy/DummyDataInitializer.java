package com.example.soulware_week1.global.dummy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DummyDataInitializer {

    private final DummyDataService dummyDataService;

    public DummyDataInitializer(DummyDataService dummyDataService) {
        this.dummyDataService = dummyDataService;
    }

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            long startTime = System.currentTimeMillis();
            int totalMembers = 300;

            for (int i = 0; i < totalMembers; i++) {
                dummyDataService.insertTestDataAsync();
            }

            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            System.out.println("Total time to insert data: " + totalTime + " ms");
        };
    }
}
