# Sample Application for Pivotal Cloud Cache
Sample application to show case HTTP Session caching with Pivotal Cloud Cache(PCC)

## About the application
This application just has a login page which always allows users to login and greets them

The username is stored in the http session.

## How to confirm Session State Caching is working


1. Run `./gradlew clean build`. This will generate a `war'
file. 
1. Using manifest.yml you can `cf push` the `jar` file.
1. While pusing the application please ensure you are 
using the java-buildpack version which supports Session State Caching(SSC), please
review Pivotal Cloud Cache(PCC) [docs](https://docs.pivotal.io/p-cloud-cache/1-3/developer.html#ssc) to ensure you have 
the right version. I tested this application with `Java Buildpack offline` version 4.8.
1. One pushed you have 2 options to test SSC feature
 - `/greeting` endpoint.
 - `/` home page.
 
### Login Page test
1. For `/greeting` endpoint, enter the username and message.
1. Click on `submit` and you should see a message with name
entered in the previous step.
1. `cf restart` the application.
1. Hit the `/greeting` endpoint again and you should be taken to the form page again, i.e. application forgot what you 
entered. 
1. Create a new Pivotal Cloud Cache(PCC) service instance using `cf create-service p-cloudcache <plan name> 
<service instance name> -t session-replication`
1. Bind the application with Service Instance created in last step. You have to restage the application.
1. Hit the `/greeting` endpoint again and now you should be taken to previous greeting entered and not the form page. 
Previous session data is now saved in Pivotal Cloud Cache(PCC).
1. To confirm all this data is stored in cache, login to PCC using `gfsh`(please note you need to use the same version 
of `gfsh` as the version of Pivotal GemFire deployed by PCC)
1. Run `list regions` command after logging in.
1. You should notice a region named `gemfire_modules_sessions`, this contains all the session data from the application.

### Home page test

1. If you hit URL `/` home page. You would be taken to static page with session ID and number of hits on `/` page.
1. If you refresh you will notice the number of hits counter increases.
1. If you `cf restart` the application and hit this endpoint again you will notice that counter starts from where it 
left. As it has saved the session data on PCC(Ensure you have the PCC service instance bound to application with correct
tag `session-replication`) 


## Troubleshooting
1. If you notice that your session objects are not getting stores in Pivotal Cloud Cache(PCC) please ensure the following
 - Check if the service instance is bound to the application, you can do that by `cf service` command.
 
 ```
 
 name   service        plan       bound apps                 last operation
 dev1   p-cloudcache   dev-plan   httpsession-gemfire-demo   create succeeded
 ```

 - If this is done, check the tag is present on the service instance, e.g. type `cf service <service name>`

 ```
 
name:            dev1
service:         p-cloudcache
tags:            session-replication
plan:            dev-plan
description:     Pivotal Cloud Cache offers the ability to deploy a GemFire cluster as a service in Pivotal Cloud Foundry.
documentation:
dashboard:       http://<YOUR DOMAIN>.cf-app.com/pulse

bound apps:
name                       binding name
httpsession-gemfire-demo

Showing status of last operation from service dev1...

status:    create succeeded
message:   Instance provisioning completed
started:   2018-05-03T12:36:43Z
updated:   2018-05-03T12:42:49Z
 ```
 - If that is also done, check if Geode jar are getting associated with your application when you bind the application 
 with the service, e.g. when using Java offline buildpack v4.8 this is what you will see 
 ```$xslt
   Downloaded app package (14.9M)
   -----> Java Buildpack v4.8 (offline) | https://github.com/cloudfoundry/java-buildpack.git#9f97630
   -----> Downloading Jvmkill Agent 1.12.0_RELEASE from https://java-buildpack.cloudfoundry.org/jvmkill/trusty/x86_64/jvmkill-1.12.0_RELEASE.so (found in cache)
   -----> Downloading Open Jdk JRE 1.8.0_162 from https://java-buildpack.cloudfoundry.org/openjdk/trusty/x86_64/openjdk-1.8.0_162.tar.gz (found in cache)
          Expanding Open Jdk JRE to .java-buildpack/open_jdk_jre (1.0s)
          JVM DNS caching disabled in lieu of BOSH DNS caching
   -----> Downloading Open JDK Like Memory Calculator 3.10.0_RELEASE from https://java-buildpack.cloudfoundry.org/memory-calculator/trusty/x86_64/memory-calculator-3.10.0_RELEASE.tar.gz (found in cache)
          Loaded Classes: 13665, Threads: 250
   -----> Downloading Client Certificate Mapper 1.5.0_RELEASE from https://java-buildpack.cloudfoundry.org/client-certificate-mapper/client-certificate-mapper-1.5.0_RELEASE.jar (found in cache)
   -----> Downloading Container Customizer 2.6.0_RELEASE from https://java-buildpack.cloudfoundry.org/container-customizer/container-customizer-2.6.0_RELEASE.jar (found in cache)
   -----> Downloading Container Security Provider 1.11.0_RELEASE from https://java-buildpack.cloudfoundry.org/container-security-provider/container-security-provider-1.11.0_RELEASE.jar (found in cache)
   -----> Downloading Spring Auto Reconfiguration 2.3.0_RELEASE from https://java-buildpack.cloudfoundry.org/auto-reconfiguration/auto-reconfiguration-2.3.0_RELEASE.jar (found in cache)
   -----> Downloading Tomcat Instance 8.5.24 from https://java-buildpack.cloudfoundry.org/tomcat/tomcat-8.5.24.tar.gz (found in cache)
          Expanding Tomcat Instance to .java-buildpack/tomcat (0.1s)
   -----> Downloading Tomcat Access Logging Support 3.2.0_RELEASE from https://java-buildpack.cloudfoundry.org/tomcat-access-logging-support/tomcat-access-logging-support-3.2.0_RELEASE.jar (found in cache)
   -----> Downloading geode-store-0.0.2.tar.gz 0.0.2 from https://repo.spring.io/ext-release-local/geode-store/geode-store-0.0.2.tar.gz (found in cache)
          Expanding geode-store-0.0.2.tar.gz to .java-buildpack/tomcat/lib (0.3s)
          Adding Geode-based Session Replication
   -----> Downloading Tomcat Lifecycle Support 3.2.0_RELEASE from https://java-buildpack.cloudfoundry.org/tomcat-lifecycle-support/tomcat-lifecycle-support-3.2.0_RELEASE.jar (found in cache)
   -----> Downloading Tomcat Logging Support 3.2.0_RELEASE from https://java-buildpack.cloudfoundry.org/tomcat-logging-support/tomcat-logging-support-3.2.0_RELEASE.jar (found in cache)
```