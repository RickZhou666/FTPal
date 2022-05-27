package com.rick.ftpal.task;

import com.rick.ftpal.entity.Environment;
import com.rick.ftpal.repository.EnvironmentRepository;
import com.rick.ftpal.util.Constants;
import com.rick.ftpal.util.SlackMessenger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IteratorUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class ScheduledTask {

    private static Integer MAX_RUNNING_HOUR = 6;

    @Autowired
    private EnvironmentRepository environmentRepository;

    public void mainTask() {
        log.info("mainTask triggered");

    }

    public void environmentMaintenanceTask() {
        List<Environment> environmentList = IteratorUtils.toList(environmentRepository.findAll().iterator());
        Date currentDate = new Date();
        for (Environment environment : environmentList) {
            // Free environment if the environment has been in ENVIRONMENT_STATUS_BUSY state for more than 6 hours
            if (Constants.ENVIRONMENT_STATUS_BUSY.equals(environment.getStatus())
                    && DateTime.now().minusHours(MAX_RUNNING_HOUR).isAfter(environment.getUpdateTime().getTime())) {
                log.info(environment.toString());
                environment.setStatus(Constants.ENVIRONMENT_STATUS_IDLE);
                environment.setUpdateTime(currentDate);
                environmentRepository.save(environment);
            }
        }
        long maintenanceCount = environmentList.stream()
                .filter(environment -> environment.getStatus().equals(Constants.ENVIRONMENT_STATUS_MAINTENANCE)).count();
        if (maintenanceCount >= 20) {
            SlackMessenger.sendMaintenanceMessage(Constants.SLACK_CHANNEL_NAME, "Maintenance threshold hit! Please check!");
        }
    }
}
