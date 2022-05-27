package com.rick.ftpal.util;

public class Constants {
    // NUMBER_OF_CI_WORKER that run in parallel
    public static final int NUMBER_OF_CI_WORKER = 3;

    // ENVIRONMENT_STATUS
    public static final String ENVIRONMENT_STATUS_IDLE = "IDLE";
    public static final String ENVIRONMENT_STATUS_BUSY = "BUSY";
    public static final String ENVIRONMENT_STATUS_MAINTENANCE = "MAINTENANCE";

    // EXECUTION_STATUS
    public static final String EXECUTION_STATUS_SUBMITTED = "SUBMITTED";
    public static final String EXECUTION_STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String EXECUTION_STATUS_COMPLETED = "COMPLETED";

    // EXECUTION_TASK_STATUS
    public static final String EXECUTION_TASK_STATUS_IN_PROGRESS = "SUBMITTED";
    public static final String EXECUTION_TASK_STATUS_COMPLETED = "COMPLETED";

    // SLACK
    public static final String SLACK_CHANNEL_NAME = "help-jdcloud";
    public static final String SLACK_ICON_URL = "https://avatars.slack-edge.com/2019-09-16/763474687542_ad93d1b41503b2df1425_96.png";
    public static final String SLACK_USER_NAME = "FunctionalTest Pal";
    public static final String RAYLIS_SLACK_TOKEN = "RAYLIS_SLACK_TOKEN";

    // TESTENV_OPERATOR]
    public static final String TESTENV_OPERATOR_NT = System.getenv("TESTENV_OPERATOR_NT");
    public static final String TESTENV_OPERATOR_AUTHORIZATION = System.getenv("TESTENV_OPERATOR_AUTHORIZATION");

    // COMPONENT
    public static final String COMPONENT_COMPLIANCEPOLICYSERV = "compliancepolicyserv";
    public static final String COMPONENT_COMPLIANCEREADSERV = "compliancereadserv";

    // CONSOLE
    public static final String CONSOLE_TESTENV_URL = "https://engineering.paypalcorp.com/console/ext/testenv/environments/";

}
