package com.jasonvillar.works.register.services;

import com.jasonvillar.works.register.entities.WorkRegister;
import com.jasonvillar.works.register.repositories.WorkRegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                message.append("\n");
            }
            message.append("serviceId must exist");
        }

        if (!this.clientService.isExistId(entity.getClientId())) {
            if (!message.isEmpty()) {
                message.append("\n");
            }
            message.append("clientId must exist");
        }

        return message.toString();
    }
}
