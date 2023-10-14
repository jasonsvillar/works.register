package com.jasonvillar.works.register.work_register.port.out;

import com.jasonvillar.works.register.client.port.out.ClientDTOAdapter;
import com.jasonvillar.works.register.work_register.port.in.WorkRegisterRequest;
import com.jasonvillar.works.register.service.port.out.ServiceDTOAdapter;
import com.jasonvillar.works.register.user.port.out.UserDTOAdapter;
import com.jasonvillar.works.register.work_register.WorkRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class WorkRegisterDTOAdapter implements Function<WorkRegister, WorkRegisterDTO> {

    private final UserDTOAdapter userDTOAdapter;
    private final ServiceDTOAdapter serviceDTOAdapter;
    private final ClientDTOAdapter clientDTOAdapter;

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
                userDTOAdapter.apply(workRegister.getUser()),
                serviceDTOAdapter.apply(workRegister.getService()),
                clientDTOAdapter.apply(workRegister.getClient())
        );
    }
}
