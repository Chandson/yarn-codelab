package sasl;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.AuthorizeCallback;
import javax.security.sasl.RealmCallback;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;

public class AuthMain {

    public static void main(String[] args) throws SaslException {

        Map<String, String> props = new TreeMap<String, String>();

        props.put(Sasl.QOP, "auth");

        SaslServer ss = Sasl.createSaslServer("DIGEST-MD5", "xmpp", "java.com",

            props, new ServerCallbackHandler());

        byte[] token = new byte[0];

        System.out.println("step1");
        byte[] challenge = ss.evaluateResponse(token);

        SaslClient sc = Sasl.createSaslClient(new String[]{"DIGEST-MD5"},

            "tony", "xmpp", "java.com", null, new ClientCallbackHandler());

        byte response[];

        if (challenge != null) {

            System.out.println("step2");
            response = sc.evaluateChallenge(challenge);

        } else {

            response = sc.evaluateChallenge(null);

        }

        System.out.println("step3");
        challenge = ss.evaluateResponse(response);
        System.out.println("step4");
        sc.evaluateChallenge(challenge);

        System.out.println("step5");
        // https://www.baeldung.com/java-sasl
        // Read source code here, you can understand why two challenges are needed.
        if (ss.isComplete()) {
            System.out.println("auth success");
            System.out.println(ss.getMechanismName());
            System.out.println(ss.getAuthorizationID());
            System.out.println(ss);
        }

        System.out.println("step6");
        System.out.println(sc.isComplete());
    }
}

class ClientCallbackHandler implements CallbackHandler {

    public void handle(Callback[] callbacks) throws IOException,

        UnsupportedCallbackException {

        System.out.println("Client: " + callbacks.length);
        for (int i = 0; i < callbacks.length; i++) {
            System.out.println("Client:" + callbacks[i]);
        }
        System.out.println("");
        for (int i = 0; i < callbacks.length; i++) {

            if (callbacks[i] instanceof NameCallback) {

                NameCallback ncb = (NameCallback) callbacks[i];

                ncb.setName("tony");

            } else if (callbacks[i] instanceof PasswordCallback) {

                PasswordCallback pcb = (PasswordCallback) callbacks[i];

                // You can change password here.
                pcb.setPassword("admin1".toCharArray());

            } else if (callbacks[i] instanceof RealmCallback) {

                RealmCallback rcb = (RealmCallback) callbacks[i];

                rcb.setText("java.com");

            } else {

                throw new UnsupportedCallbackException(callbacks[i]);

            }

        }

    }

}

class ServerCallbackHandler implements CallbackHandler {

    public ServerCallbackHandler() {

    }

    public void handle(final Callback[] callbacks) throws IOException,

        UnsupportedCallbackException {

        System.out.println("Server: " + callbacks.length);
        for (int i = 0; i < callbacks.length; i++) {
            System.out.println("Server:" + callbacks[i]);
        }
        System.out.println("");
        for (Callback callback : callbacks) {

            if (callback instanceof RealmCallback) {

                //do your business

            } else if (callback instanceof NameCallback) {

                //do your business

            } else if (callback instanceof PasswordCallback) {

                ((PasswordCallback) callback).setPassword("admin1"

                    .toCharArray());

            } else if (callback instanceof AuthorizeCallback) {

                AuthorizeCallback authCallback = ((AuthorizeCallback) callback);

                authCallback.setAuthorized(true);

            } else {

                System.out.println(callback.getClass().getName());

                throw new UnsupportedCallbackException(callback,

                    "Unrecognized Callback");

            }

        }

    }

}
