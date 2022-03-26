package jass.kerberos;

import java.io.File;
import java.security.PrivilegedAction;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

public class KerberosClient {

    public static void main(String[] args) {
        Subject subject = new Subject();
        Krb5Configuration conf = new Krb5Configuration();
        try {
            String sep = File.separator;
            System.out.println("KerberosClient.main():"
                + System.getProperty("java.home") + sep + "lib" + sep
                + "security" + sep + "java.security");
            LoginContext context = new LoginContext("myKerberosLogin", subject,
                null, conf);
            context.login();
            System.out.println(context.getSubject().getPrincipals());
            System.out.println(subject);

            subject.doAs(subject, (PrivilegedAction<Object>) () -> {
                System.out.println("Hello world");
                System.out
                    .println(Thread.currentThread().getContextClassLoader());
                return null;
            });

        } catch (LoginException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
