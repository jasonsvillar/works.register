package com.jasonvillar.works.register.work_register;

import com.jasonvillar.works.register.client.ClientService;
import com.jasonvillar.works.register.work_register.WorkRegister;
import com.jasonvillar.works.register.work_register.WorkRegisterRepository;
import com.jasonvillar.works.register.service.ServiceService;
import com.jasonvillar.works.register.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkRegisterService {
    private final WorkRegisterRepository repository;
    private final UserService userService;
    private final ClientService clientService;
    private final ServiceService serviceService;

    public List<WorkRegister> getList() {
        return this.repository.findAll();
    }

    public List<WorkRegister> getListByUserId(long userId) {
        return this.repository.findAllByUserId(userId);
    }

    public List<WorkRegister> getListByClientId(long clientId) {
        return this.repository.findAllByClientId(clientId);
    }

    public List<WorkRegister> getListByUserIdAndClientId(long userId, long clientId) {
        return this.repository.findAllByUserIdAndClientId(userId, clientId);
    }

    public List<WorkRegister> getListByTitleLike(String title) {
        return this.repository.findAllByTitleContainingIgnoreCase(title);
    }

    public List<WorkRegister> getListByTitleLikeAndUserId(String title, long userId) {
        return this.repository.findAllByTitleContainingIgnoreCaseAndUserId(title, userId);
    }

    public WorkRegister getById(long id) {
        return this.repository.findWorkRegisterById(id);
    }

    public Optional<WorkRegister> getOptionalById(long id) {
        return this.repository.findOptionalById(id);
    }

    public WorkRegister save(WorkRegister workRegister) {
        return this.repository.save(workRegister);
    }

    public String getValidationsMessageWhenCantBeSaved(WorkRegister entity) {
        StringBuilder message = new StringBuilder();

        if (!this.userService.isExistId(entity.getUserId())) {
            message.append("userId must exist");
        }

        if (!this.serviceService.isExistId(entity.getServiceId())) {
            if (!message.isEmpty()) {
                message.append(", ");
            }
            message.append("serviceId must exist");
        }

        if (!this.clientService.isExistId(entity.getClientId())) {
            if (!message.isEmpty()) {
                message.append(", ");
            }
            message.append("clientId must exist");
        }

        return message.toString();
    }

    public Optional<WorkRegister> getOptionalByIdAndUserId(long id, long userId) {
        return this.repository.findOptionalByIdAndUserId(id, userId);
    }
}
