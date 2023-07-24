package com.business.automation.MicroService1.truelicense;


import global.namespace.truelicense.api.ConsumerLicenseManager;
import global.namespace.truelicense.api.LicenseManagementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ReadLicenceController {

    private static ConsumerLicenseManager manager() { return LicenseManager.get(); }

    //private static final String LICENSE_FILE_PATH = "/Users/industry4/Documents/Spring Boot Project/Licence/TestLicence11/stargazer/license.lic";


//    @PostMapping("licence-install")
//    public ResponseEntity<?> installLicence(@RequestBody @Validated InstallLicenceBean bean) throws LicenseManagementException {
//        manager().install(file(bean.getLicencePath()));
//        return new ResponseEntity<>("Ok", HttpStatus.OK);
//    }

    @GetMapping("licence-uninstall")
    public ResponseEntity<?> unInstallLicence() throws LicenseManagementException{
        manager().uninstall();
        return new ResponseEntity<>("Ok", HttpStatus.OK);
    }

    @GetMapping("licence-info")
    public ResponseEntity<?> getLicenceInfo() throws LicenseManagementException {
        return new ResponseEntity<>(manager().load(), HttpStatus.OK);
    }


}


