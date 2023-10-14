package com.jasonvillar.works.register.work_register.port.out;

import com.jasonvillar.works.register.client.port.out.ClientDTOMapper;
import com.jasonvillar.works.register.work_register.port.in.WorkRegisterRequest;
import com.jasonvillar.works.register.service.port.out.ServiceDTOMapper;
import com.jasonvillar.works.register.user.port.out.UserDTOMapper;
import com.jasonvillar.works.register.work_register.WorkRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class WorkRegisterDTOMapper implements Function<WorkRegister, WorkRegisterDTO> {

    private final UserDTOMapper userDTOMapper;
    private final ServiceDTOMapper serviceDTOMapper;
    private final ClientDTOMapper clientDTOMapper;

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
                userDTOMapper.apply(workRegister.getUser()),
                serviceDTOMapper.apply(workRegister.getService()),
                clientDTOMapper.apply(workRegister.getClient())
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
