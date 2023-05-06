package com.jasonvillar.works.register.dto.workregister;

import com.jasonvillar.works.register.dto.client.ClientMapper;
import com.jasonvillar.works.register.dto.service.ServiceMapper;
import com.jasonvillar.works.register.dto.user.UserMapper;
import com.jasonvillar.works.register.entities.WorkRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class WorkRegisterMapper implements Function<WorkRegister, WorkRegisterDTO> {

    private final UserMapper userMapper;
    private final ServiceMapper serviceMapper;
    private final ClientMapper clientMapper;

    @Override
    public WorkRegisterDTO apply(WorkRegister workRegister) {
        return new WorkRegisterDTO(
                workRegister.getId(),
                workRegister.getTitle(),
                workRegister.getDateFrom(),
                workRegister.getTimeFrom(),
                workRegister.getDateTo(),
                workRegister.getTimeTo(),
                workRegister.getPayment(),
                userMapper.apply(workRegister.getUser()),
                serviceMapper.apply(workRegister.getService()),
                clientMapper.apply(workRegister.getClient())
        );
    }

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
