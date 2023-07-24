package com.business.automation.MicroService1.truelicense;

import global.namespace.truelicense.api.ConsumerLicenseManager;
import global.namespace.truelicense.api.LicenseManagementException;
import global.namespace.truelicense.swing.LicenseManagementWizard;

import javax.swing.*;
import java.util.*;

import static global.namespace.fun.io.bios.BIOS.file;
import static java.lang.System.*;

public enum Main {

    USAGE {
        @Override
        void run(final Deque<String> params) { throw new IllegalArgumentException(); }
    },

    HELP {
        @Override
        void run(final Deque<String> params) { out.printf(Main.valueOf(params.pop().toUpperCase(Locale.ROOT)).help()); }
    },

    VERSION {
        @Override
        void run(final Deque<String> params) { out.printf(message("version"), Main.class.getSimpleName()); }
    },

    INSTALL {
        @Override
        void run(Deque<String> params) throws LicenseManagementException { manager().install(file(params.pop())); }
    },

    LOAD {
        @Override
        void run(Deque<String> params) throws LicenseManagementException { out.println(manager().load()); }
    },

    VERIFY {
        @Override
        void run(Deque<String> params) throws LicenseManagementException { manager().verify(); }
    },

    UNINSTALL {
        @Override
        void run(Deque<String> params) throws LicenseManagementException { manager().uninstall(); }
    },


    WIZARD {
        @Override
        void run(Deque<String> params) throws Exception {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override public void run() {
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (Exception ex) {
                        throw new AssertionError(ex);
                    }
                    final LicenseManagementWizard wizard = new LicenseManagementWizard(manager());
                    wizard.setUninstallButtonVisible(true);
                    exit(wizard.showModalDialog());
                }
            });
        }
    };

    private static ConsumerLicenseManager manager() { return LicenseManager.get(); }


    abstract void run(Deque<String> params) throws Exception;

    public static void main(String... args) { exit(processAndHandleExceptions(args)); }

    @SuppressWarnings("CallToThreadDumpStack")
    private static int processAndHandleExceptions(final String... args) {
        int status;
        try {
            process(args);
            status = 0;
        } catch (final IllegalArgumentException ex) {
            printUsage();
            status = 1;
        } catch (final NoSuchElementException ex) {
            printUsage();
            status = 1;
        } catch (final Throwable ex) {
            ex.printStackTrace();
            status = 2;
        }
        return status;
    }

    public static void process(final String... args) throws Exception {
        final Deque<String> params = new LinkedList<String>(Arrays.asList(args));
        final String command = upperCase(params.pop());
        valueOf(command).run(params);
    }




    private static String upperCase(String s) { return s.toUpperCase(Locale.ENGLISH); }

    private static void printUsage() {
        final StringBuilder builder = new StringBuilder(25 * 80);
        for (final Main main : values()) {
            builder.append(main.usage());
        }
        err.println(builder.toString());
    }

    private String usage() { return String.format(message("usage"), Main.class.getSimpleName()); }

    private String help() { return message("help"); }

    String message(String key) {
        return ResourceBundle
                .getBundle(Main.class.getName())
                .getString(name() + "." + key);
    }
}
