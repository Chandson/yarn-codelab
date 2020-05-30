package com.chandson.infra;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Container {
    private static final Log LOG = LogFactory.getLog(Container.class);

    public static void main(String args[]) {
        LOG.info("Hello, chandson!");
        LOG.info("This is your first container!");
        LOG.info("current args:");
        for(int i=0 ; i<args.length; i++) {
            LOG.info(args[i]);
        }
    }
}
