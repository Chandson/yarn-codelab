package com.chandson.infra;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;

public class CommandLineTest {
    public static void main(String args[]) throws Exception {
        Options opts = new Options();
        opts.addOption("shell_env", true, "Environment for shell script. Specified as env_key=env_val pairs");
        // usage: --shell_env key1=value1 --shell_env key2=value2
        CommandLine cliParser = new GnuParser().parse(opts, args);
        if (cliParser.hasOption("shell_env")) {
            String envs[] = cliParser.getOptionValues("shell_env");
            for(String env: envs) {
                System.out.println(env);
            }
        }
    }
}
