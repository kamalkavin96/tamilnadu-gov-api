package com.kamalkavin96.tamilnadu_gov_api.services;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;

import com.kamalkavin96.tamilnadu_gov_api.clients.tnpds.PDSReportClient;
import com.kamalkavin96.tamilnadu_gov_api.dto.api.StateReportDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PdsSyncService2 {

        private final PDSReportClient pdsReportClient;

        public void run() {



        }

}
