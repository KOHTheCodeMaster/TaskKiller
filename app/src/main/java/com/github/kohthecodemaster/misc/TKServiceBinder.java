package com.github.kohthecodemaster.misc;

import com.github.kohthecodemaster.service.TaskKillerAccessibilityService;

@FunctionalInterface
public interface TKServiceBinder {

    void emitService(TaskKillerAccessibilityService service);

}
