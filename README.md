  ${project.basedir}/dependency/rni-es-7.13.2.1.jar
  ${project.basedir}/dependency/rni-rnt-bundle-7.35.2.c65.0.jar
  ${project.basedir}/dependency/common-api-37.2.0.jar
  ${project.basedir}/dependency/common-lib-38.0.4.jar
  ${project.basedir}/dependency/slf4j-api-1.7.21.jar

These maven commands aren't working for me now
mvn install:install-file -Dfile=dependency/rni-es-7.13.2.1.jar -DgroupId=namesearch.rest -DartifactId=rni-es -Dversion=1.0 -Dpackaging=jar

mvn install:install-file -Dfile=dependency/rni-rnt-bundle-7.35.2.c65.0.jar -DgroupId=namesearch.rest-DartifactId=rni-rnt-bundle -Dversion=1.0 -Dpackaging=jar

These 5 files either have to be installed in your m2, or in the current pom implementation, put the jar files in a directory called /dependency/