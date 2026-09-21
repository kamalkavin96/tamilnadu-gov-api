package com.kamalkavin96.tamilnadu_gov_api;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kamalkavin96.tamilnadu_gov_api.clients.tnpds.PDSReportClient;


@SpringBootTest
class TamilnaduGovApiApplicationTests {

	@Autowired
	private PDSReportClient pdsReportClient;

	@Test
	void contextLoads() throws IOException, InterruptedException {
		String data = pdsReportClient.getStateListReport();
		System.out.println(data);
	}

}
