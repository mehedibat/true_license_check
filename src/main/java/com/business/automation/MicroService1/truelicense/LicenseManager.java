package com.business.automation.MicroService1.truelicense;

import global.namespace.fun.io.api.Source;
import global.namespace.truelicense.api.*;
import global.namespace.truelicense.api.passwd.PasswordProtection;
import global.namespace.truelicense.core.passwd.ObfuscatedPasswordProtection;
import global.namespace.truelicense.obfuscate.ObfuscatedString;
import global.namespace.truelicense.v4.V4;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


public enum LicenseManager implements ConsumerLicenseManager {
    standard {
        @Override
        ConsumerLicenseManager newManager() {
            String KEY_STORE_FILE = "public.ks";
            InputStream inputStream = LicenseManager.class.getClassLoader().getResourceAsStream(KEY_STORE_FILE);
            File file = inputStreamToFile(inputStream);
            return _managementContext
                    .consumer()
                    .encryption()
                    .protection(protection(new long[] { 0xe3bf675b5f118328L, 0x37ff034864812da5L, 0xe8c64dc5c1f5ed66L }) /* => "Test404NotFound" */)
                    .up()
                    .authentication()
                    .alias(name())
                    .loadFromResource(KEY_STORE_FILE)
                    .storeProtection(protection(new long[] { 0x47bd7841295e934L, 0x69ab175544b9e49aL, 0xf7858a6ae41d82a3L }) /* => "Test404NotFound" */)
                    .up()
                    .storeInUserPreferences(Main.class) // must be a non-obfuscated class!
                    .build();
        }
    };

//    @Obfuscate
//    private static final String KEY_STORE_FILE = "/Users/industry4/Documents/Spring Boot Project/Licence/TestLicence11/stargazer/keymgr/target/classes/public.ks";
    //private static final String KEY_STORE_FILE = "/Users/industry4/Documents/public.ks";

    private static final String SUBJECT = "TestGear404noTfounD";

    private static final LicenseManagementContext _managementContext = V4
            .builder()
            .subject(SUBJECT)
            .build();

    public volatile ConsumerLicenseManager _manager;

    /**
     * Returns the first license manager in the configured
     * chain-of-responsibility, which is {@link #standard}.
     * <p>
     * By default, this manager should be used to
     * , {@linkplain #load() load} and
     * {@linkplain #uninstall() uninstall} license keys.
     */
    public static LicenseManager get() { return standard; }

    private ConsumerLicenseManager manager() {
        // No need to synchronize because managers are virtually stateless.
        final ConsumerLicenseManager m = _manager;
        return null != m ? m : (_manager = newManager());
    }

    abstract ConsumerLicenseManager newManager();

    private static PasswordProtection protection(long[] obfuscated) {
        return new ObfuscatedPasswordProtection(new ObfuscatedString(obfuscated));
    }

    @Override
    public LicenseManagerParameters parameters() { return manager().parameters(); }

    @Override
    public void install(Source source) throws LicenseManagementException {
        manager().install(source);
    }

    @Override
    public License load() throws LicenseManagementException {
        return manager().load();
    }

    @Override
    public void verify() throws LicenseManagementException {
        manager().verify();
    }

    @Override
    public void uninstall() throws LicenseManagementException {
        manager().uninstall();
    }


    public static File inputStreamToFile(InputStream inputStream)  {



        try {
            File tempFile = File.createTempFile("temp", ".tmp");
            tempFile.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            return tempFile;
        } catch (Exception ex) {
            throw new RuntimeException("ex",ex);
        }

    }
}
