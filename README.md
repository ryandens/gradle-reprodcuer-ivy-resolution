# gradle-reprodcuer-ivy-resolution

The following command passes on Gradle 8.1 and the latest nightly builds of Gradle 8.2. It fails on `gradle-8.2-20230422130248+0000`. To demonstrate the failure, check out the commit `dcd22ba5674c81657e9d4115f0ad473ee011941b` and run the `syncTrivy` task

```bash
$ ./gradlew syncTrivy 

```