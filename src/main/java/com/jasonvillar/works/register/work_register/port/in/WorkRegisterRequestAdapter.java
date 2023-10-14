package com.jasonvillar.works.register.work_register.port.in;

import com.jasonvillar.works.register.work_register.WorkRegister;
import org.springframework.stereotype.Component;

@Component
public class WorkRegisterRequestAdapter {
    public WorkRegister toEntity(WorkRegisterRequest workRegisterRequest) {
        return new WorkRegister(
                workRegisterRequest.title(),
                workRegisterRequest.dateFrom(),
                workRegisterRequest.timeFrom(),
                workRegisterRequest.dateTo(),
                workRegisterRequest.timeTo(),
                workRegisterRequest.payment(),
                workRegisterRequest.userId(),
                workRegisterRequest.serviceId(),
                workRegisterRequest.clientId()
        );
    }
}
