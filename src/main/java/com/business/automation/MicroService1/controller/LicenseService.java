package com.business.automation.MicroService1.controller;

import com.business.automation.MicroService1.truelicense.LicenseManager;
import com.business.automation.MicroService1.truelicense.ReadLicenceController;
import com.business.automation.MicroService1.web.WebService;
import com.fasterxml.jackson.databind.JsonNode;
import global.namespace.truelicense.api.ConsumerLicenseManager;
import global.namespace.truelicense.api.LicenseManagementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static global.namespace.fun.io.bios.BIOS.file;

@Slf4j
@Service
public class LicenseService {

    private final WebService webService;
    private final ReadLicenceController readLicenceController;

    @Value("${license.path}")
    String licensePath;
    String trueLicenseFilaName = "license.lic";
    String licenseDownloadUrl = "http://localhost:8899/download-license";

    private static ConsumerLicenseManager manager() { return LicenseManager.get(); }

    public LicenseService(WebService webService, ReadLicenceController readLicenceController) {
        this.webService = webService;
        this.readLicenceController = readLicenceController;
    }

    public void downloadLicenseAndInstall() throws IOException, LicenseManagementException {


        try {
            if (manager().load() != null) {
                log.info("True License Already Install");
                return;
            }
        } catch (Exception e) {
            log.error("Exception downloadLicenseAndInstall => " + e);
        }
        log.info("True License Install");
        ///       Get lic file from True License Api
        JsonNode jsonResponse = webService.getJsonResponse(licenseDownloadUrl);
        byte[] fileBytes = jsonResponse.get("body").binaryValue();;
        System.out.println(fileBytes);

        Path folderPath = Paths.get(licensePath);
        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }

        // Save the file to the folder
        Path filePath = folderPath.resolve(trueLicenseFilaName);
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            fos.write(fileBytes);
        }

        // Install license
        manager().install(file(licensePath + "/" + trueLicenseFilaName));


    }

    public void downloadNewLicenseAndInstall() throws IOException, LicenseManagementException {

        log.info("Install");
        ///       Get lic file from True License Api
        JsonNode jsonResponse = webService.getJsonResponse(licenseDownloadUrl);
        byte[] fileBytes = jsonResponse.get("body").binaryValue();;
        System.out.println(fileBytes);

        Path folderPath = Paths.get(licensePath);
        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }

        // Save the file to the folder
        Path filePath = folderPath.resolve(trueLicenseFilaName);
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            fos.write(fileBytes);
        }

        // Install license
        manager().install(file(licensePath));

    }
}
