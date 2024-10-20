package com.raillylinker.module_dpd_scheduler.configurations

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.config.ScheduledTaskRegistrar

@Configuration
class SchedulerConfig : SchedulingConfigurer {
    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        val threadPoolTaskScheduler = ThreadPoolTaskScheduler()

        threadPoolTaskScheduler.poolSize = 10
        threadPoolTaskScheduler.setThreadNamePrefix("my-scheduled-task-pool")
        threadPoolTaskScheduler.initialize()

        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler)
    }
}