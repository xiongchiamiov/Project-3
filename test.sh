#!/bin/sh

#javac -cp libs/uispec4j-2.4-jdk16.jar:/usr/share/java/junit.jar:libs/GridGame3.4.jar:.  MahjonggTest.java MinesTest.java
java -cp libs/uispec4j-2.4-jdk16.jar:/usr/share/java/junit.jar:libs/GridGame3.4.jar:.  org.junit.runner.JUnitCore $1Test

