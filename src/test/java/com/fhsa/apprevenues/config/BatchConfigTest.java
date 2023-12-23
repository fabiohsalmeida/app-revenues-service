package com.fhsa.apprevenues.config;

import com.fhsa.apprevenues.AppRevenuesApplication;
import com.fhsa.apprevenues.util.AppRevenuesTestDataProviderUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.fhsa.apprevenues.util.AppRevenuesTestDataProviderUtils.supplyValidAppCompaniesContent;

@SpringBatchTest
@SpringJUnitConfig({AppRevenuesApplication.class, BatchConfig.class})
@ExtendWith(SpringExtension.class)
public class BatchConfigTest {

    private static final Path INPUT_DIRECTORY = Path.of("target/input-tests");

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        if (Files.notExists(INPUT_DIRECTORY)) {
            Files.createDirectories(INPUT_DIRECTORY);
        }

        jobRepositoryTestUtils.removeJobExecutions();
    }

    @AfterEach
    void tearDown() {

    }
//
//    @Test
//    @SneakyThrows
//    @DisplayName("GIVEN test WHEN test THEN test")
//    void test() {
//        // GIVEN
//        Path shouldCompleteFilePath = Path.of(INPUT_DIRECTORY + File.separator + "app-companies.csv");
//        Path inputFile = Files.createFile(shouldCompleteFilePath);
//
//        Files.writeString(inputFile, supplyValidAppCompaniesContent());
//
//        var jobParameters = new JobParametersBuilder()
//                .addString("$")
//                .
//        // WHEN
//
//
//    }
}
