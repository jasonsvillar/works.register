package com.jasonvillar.works.register.work_register.port.in;

import com.jasonvillar.works.register.work_register.WorkRegister;
import org.springframework.stereotype.Component;

@Component
public class WorkRegisterRequestAdapter {
    public WorkRegister toEntity(WorkRegisterRequest workRegisterRequest) {
        return WorkRegister.builder()
                .title(workRegisterRequest.title())
                .dateFrom(workRegisterRequest.dateFrom())
                .timeFrom(workRegisterRequest.timeFrom())
                .dateTo(workRegisterRequest.dateTo())
                .timeTo(workRegisterRequest.timeTo())
                .payment(workRegisterRequest.payment())
                .serviceId(workRegisterRequest.serviceId())
                .clientId(workRegisterRequest.clientId())
                .build();
    }
}
