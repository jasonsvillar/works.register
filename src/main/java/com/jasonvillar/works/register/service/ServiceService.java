package com.jasonvillar.works.register.service;

import com.jasonvillar.works.register.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService {
    private final ServiceRepository serviceRepository;

    public List<Service> getList(int pageNumber, int rows) {
        Pageable page = PageRequest.of(pageNumber, rows, Sort.by("name"));
        return this.serviceRepository.findAll(page).stream().toList();
    }

    public List<Service> getListByUserId(long userId, int pageNumber, int rows) {
        Pageable page = PageRequest.of(pageNumber, rows, Sort.by("name"));
        return this.serviceRepository.findAllByUserId(userId, page);
    }

    public long getRowCountByUserId(long userId) {
        return this.serviceRepository.countByUserId(userId);
    }

    public long getRowCount() {
        return this.serviceRepository.count();
    }

    public Service getById(long id) {
        return this.serviceRepository.findServiceById(id);
    }

    public Optional<Service> getOptionalById(long id) {
        return this.serviceRepository.findOptionalById(id);
    }

    public Optional<Service> getOptionalByNameAndUserId(String name, long userId) {
        return this.serviceRepository.findOptionalByNameAndUserId(name, userId);
    }

    public boolean isExistId(long id) {
        return this.getOptionalById(id).isPresent();
    }

    public boolean isExistNameAndUserId(String name, long userId) {
        return this.getOptionalByNameAndUserId(name, userId).isPresent();
    }

    public String getValidationsMessageWhenCantBeSaved(Service entity) {
        StringBuilder message = new StringBuilder();

        if (this.isExistNameAndUserId(entity.getName(), entity.getUser().getId())) {
            message.append("name must be unique");
        }

        return message.toString();
    }

    public Service save(Service service) {
        return this.serviceRepository.save(service);
    }

    public Optional<Service> getOptionalByIdAndUserId(long id, long userId) {
        return this.serviceRepository.findOptionalByIdAndUserId(id, userId);
    }

    public List<Service> getListByNameLikeAndUserId(String name, long userId) {
        return this.serviceRepository.findAllByNameContainingIgnoreCaseAndUserId(name, userId);
    }

    public List<Service> getListByIdListAndUserIdAndNotInWorkRegister(List<Long> idList, long userId) {
        return this.serviceRepository.findAllByIdAndUserIdAndServiceNotInWorkRegister(idList, userId);
    }

    @Transactional
    public void deleteByServiceList(List<Service> serviceList) {
        this.serviceRepository.deleteAll(serviceList);
    }

    @Transactional
    public boolean deleteByServiceIdAndUserId(long id, long userId) {
        return this.serviceRepository.deleteByIdAndUserId(id, userId) > 0;
    }
}
