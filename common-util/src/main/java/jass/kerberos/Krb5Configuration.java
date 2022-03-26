package jass.kerberos;

import java.util.HashMap;
import java.util.Map;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.security.auth.login.Configuration;

public class Krb5Configuration extends Configuration {

    private AppConfigurationEntry[] entry = new AppConfigurationEntry[1];

    Map paramMap = new HashMap();

    private AppConfigurationEntry krb5LoginModule = new AppConfigurationEntry(
        "com.sun.security.auth.module.Krb5LoginModule",
        LoginModuleControlFlag.REQUIRED, paramMap);

    @Override
    public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
        if (entry[0] == null) {
            paramMap.put("debug", "true");
            paramMap.put("storeKey", "false");
            paramMap.put("doNotPrompt", "true");
            paramMap.put("useKeyTab", "false");
            paramMap.put("useTicketCache", "true");

            entry[0] = krb5LoginModule;
        }
        return entry;
    }
}
