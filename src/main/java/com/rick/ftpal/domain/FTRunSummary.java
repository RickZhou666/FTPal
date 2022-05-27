package com.rick.ftpal.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FTRunSummary {
    private String componentName;
    private String pullRequestId;
    private String total;
    private String pass;
    private String notStarted;
    private String failed;
    private String skipped;
    private String knownFailure;
    private String prOwner;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TOTAL: ");
        sb.append(total);
        sb.append(" | PASS: ");
        sb.append(pass);
        sb.append(" | NOT_START: ");
        sb.append(notStarted);
        sb.append(" | FAIL: ");
        sb.append(failed);
        sb.append(" | SKIP: ");
        sb.append(skipped);
        sb.append(" | KNOWN_FAIL: ");
        sb.append(knownFailure);
        return sb.toString();
    }
}
