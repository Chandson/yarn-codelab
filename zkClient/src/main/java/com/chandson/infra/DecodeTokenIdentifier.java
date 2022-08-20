package com.chandson.infra;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.security.token.TokenIdentifier;
import org.apache.hadoop.yarn.security.AMRMTokenIdentifier;
import org.apache.hadoop.yarn.security.NMTokenIdentifier;

public class DecodeTokenIdentifier {
    private static final Log LOG = LogFactory.getLog(DecodeTokenIdentifier.class);

    public static void main(String args[]) throws IOException {
        // am rm token
        AMRMTokenIdentifier amrmTokenIdentifier = new AMRMTokenIdentifier();
        decodeIdentifier(amrmTokenIdentifier, "AAABgrdi3XQAAAABAAAAAeEsOeg=");
        LOG.info(String.valueOf(amrmTokenIdentifier.getApplicationAttemptId()));
        LOG.info(String.valueOf(amrmTokenIdentifier.getKeyId()));

        // nm token
        NMTokenIdentifier nmTokenIdentifier = new NMTokenIdentifier();
        decodeIdentifier(nmTokenIdentifier, "AAABgrdi3XQAAAABAAAAAQAabjEwLTE2MS0yMTQuYnl0ZWQub3JnOjgwNTIACmxpbnlvdXF1YW5k7Wy/");
        LOG.info(nmTokenIdentifier.getApplicationAttemptId());
        LOG.info(nmTokenIdentifier.getApplicationSubmitter());
        LOG.info(nmTokenIdentifier.getKeyId());
    }

    static void decodeIdentifier(TokenIdentifier tokenIdentifier, String identifier)
        throws IOException {
        byte []identifierBytes = Base64.decodeBase64(identifier.getBytes());
        // LOG.info(new String(identifierBytes));
        ByteArrayInputStream buf = new ByteArrayInputStream(identifierBytes);
        DataInputStream in = new DataInputStream(buf);
        tokenIdentifier.readFields(in);
        in.close();
    }
}
