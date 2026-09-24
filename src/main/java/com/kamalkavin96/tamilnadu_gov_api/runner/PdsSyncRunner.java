package com.kamalkavin96.tamilnadu_gov_api.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.kamalkavin96.tamilnadu_gov_api.services.PdsSyncService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PdsSyncRunner implements CommandLineRunner {

    private static final String LOG_SEPARATOR = "==============================================";

    private final PdsSyncService pdsSyncService;

    @Override
    public void run(String... args) throws Exception {

        log.info(LOG_SEPARATOR);
        log.info("Starting Tamil Nadu PDS data synchronization");
        log.info(LOG_SEPARATOR);

        // pdsSyncService.syncStates();

        // pdsSyncService.syncDistricts();

        pdsSyncService.syncTaluks();

        log.info(LOG_SEPARATOR);
        log.info("Tamil Nadu PDS synchronization completed");
        log.info(LOG_SEPARATOR);


        System.exit(0);
    }
}