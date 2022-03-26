## 用法
### SampleAcn 启动
```
-Djava.security.auth.login.config==common-util/src/main/resources/sample_jaas.config
 -Djava.security.manager -Djava.security.policy==common-util/src/main/resources/sampleacn.policy

```

### jgss 启动
- 注意 java 版本
#### Server 启动
```
/opt/xxx/jdk/openjdk-1.8.0_265/bin/java -cp common-util/target/common-util-1.0-SNAPSHOT.jar \
 -Djava.security.krb5.realm=EXAMPLE.COM -Djava.security.krb5.kdc=xxxx \
 -Djavax.security.auth.useSubjectCredsOnly=false \
 -Djava.security.auth.login.config=/data01/home/linyouquan/infra/yarn-codelab/common-util/src/main/resources/bcsLogin.conf \
 jgss.SampleServer 4096
```
#### Client 启动
```
/opt/xxx/jdk/openjdk-1.8.0_265/bin/java -cp common-util/target/common-util-1.0-SNAPSHOT.jar \
 -Djava.security.krb5.realm=EXAMPLE.COM -Djava.security.krb5.kdc=xxxx \
 -Djavax.security.auth.useSubjectCredsOnly=false \
 -Djava.security.auth.login.config=/data01/home/linyouquan/infra/yarn-codelab/common-util/src/main/resources/bcsLogin.conf \
 jgss.SampleClient linyouquan@EXAMPLE.COM 127.0.0.1 4096
```
 
## 参考
### jaas 用法
- https://docs.oracle.com/javase/7/docs/technotes/guides/security/jaas/JAASRefGuide.html
- https://docs.oracle.com/javase/7/docs/technotes/guides/security/jaas/tutorials/GeneralAcnOnly.html
- https://docs.oracle.com/javase/7/docs/technotes/guides/security/jaas/tutorials/GeneralAcnAndAzn.html
- https://docs.oracle.com/javase/8/docs/technotes/guides/security/jgss/tutorials/BasicClientServer.html
