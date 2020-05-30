package com.chandson.infra;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.yarn.api.records.LocalResource;
import org.apache.hadoop.yarn.api.records.LocalResourceType;
import org.apache.hadoop.yarn.api.records.LocalResourceVisibility;
import org.apache.hadoop.yarn.util.ConverterUtils;
import org.apache.hadoop.yarn.util.Records;

import java.io.IOException;
import java.util.Map;

public class Util {
    public static final String USER_DIR = "user";
    public static final String STAGING_DIR = ".staging";
    public static final String APP_HDFS_JAR = "simpleAppOnYarn-1.0-SNAPSHOT.jar";
    public static final String APP_MASTER_MAIN_CLASS = "com.chandson.infra.ApplicationMaster";
    public static final String APP_CONTAINER_MAIN_CLASS = "com.chandson.infra.Container";

    public static void addToLocalResources(FileSystem fs, String fileSrcPath,
                                           String fileDstPath, String appId, Map<String, LocalResource> localResources,
                                           String resources) throws IOException {
        String suffix = STAGING_DIR +"/" + appId + "/" + fileDstPath;
        Path dst = new Path(fs.getHomeDirectory(), suffix);
        if (fileSrcPath == null) {
            FSDataOutputStream ostream = null;
            try {
                ostream = FileSystem.create(fs, dst, new FsPermission((short) 0710));
                ostream.writeUTF(resources);
            } finally {
                IOUtils.closeQuietly(ostream);
            }
        } else {
            fs.copyFromLocalFile(new Path(fileSrcPath), dst);
        }

        FileStatus scFileStatus = fs.getFileStatus(dst);
        LocalResource scRsrc =
                LocalResource.newInstance(
                        ConverterUtils.getYarnUrlFromURI(dst.toUri()),
                        LocalResourceType.FILE, LocalResourceVisibility.APPLICATION,
                        scFileStatus.getLen(), scFileStatus.getModificationTime());
        localResources.put(fileDstPath, scRsrc);
    }


    public static LocalResource createLocalResourceOfFile(Configuration yarnConf,
                                                          String resource) throws IOException {
        LocalResource localResource = Records.newRecord(LocalResource.class);

        Path resourcePath = new Path(resource);

        FileStatus jarStat = FileSystem.get(resourcePath.toUri(), yarnConf).getFileStatus(resourcePath);
        localResource.setResource(ConverterUtils.getYarnUrlFromPath(resourcePath));
        localResource.setSize(jarStat.getLen());
        localResource.setTimestamp(jarStat.getModificationTime());
        localResource.setType(LocalResourceType.FILE);
        localResource.setVisibility(LocalResourceVisibility.APPLICATION);
        return localResource;
    }

}
