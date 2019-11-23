# sbt
Repo for cognira curated sbt docker image.

The Jenkins pipeline is responsible for building and publishing the image to our Azure container registry.

Depending on the branch we're in the image will be tagged as `cognira/sbt:latest` if in master or `cognira/sbt:BRANCH_NAME` if in different branch. Example: `cognira/sbt:DAF-103`

## Build steps

### 1. Create volume for jar cache
If it doesn't already exist, this will create a volume for the Scala .ivy2 cache. It can be reused across projects and maintains a cache of dependent jars.
``` ./create-sbt-data.sh ```

### 2. Build image

Build sbt image to build Spark application from the root direcory.
```docker build -t cognira/sbt  . ```

### 3. Run image

* The `sbt-data` volume used here is for re-using dependencies
* `--mount` is for access to source, project and target directories.

``` docker run -v sbt-data:/root/.ivy2 --mount type=bind,src=$(pwd),dst=/opt -w /opt --rm cognira/sbt:latest sbt sbtVersion```
