package com.business.automation.MicroService1;

import com.business.automation.MicroService1.controller.LicenseController;
import com.business.automation.MicroService1.controller.LicenseService;
import global.namespace.truelicense.api.LicenseManagementException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.IOException;

@SpringBootApplication
public class MicroService1Application {

	private final LicenseService licenseService;

	public MicroService1Application(LicenseService licenseController) {
		this.licenseService = licenseController;
	}

	public static void main(String[] args) {
		SpringApplication.run(MicroService1Application.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void startupSetting() throws IOException, LicenseManagementException {
		System.out.println("DownLoad file from true license service and install");
		licenseService.downloadLicenseAndInstall();
	}

}
