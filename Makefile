
build:
	mvn clean install

deploy:
	cd ${CURDIR}/infra; cdk deploy

.PHONY := build deploy