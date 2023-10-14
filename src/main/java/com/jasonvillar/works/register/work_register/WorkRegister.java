package com.jasonvillar.works.register.work_register;

import com.jasonvillar.works.register.client.Client;
import com.jasonvillar.works.register.service.Service;
import com.jasonvillar.works.register.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class WorkRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "work_register_id_seq")
    @SequenceGenerator(name = "work_register_id_seq", sequenceName = "work_register_id_seq", allocationSize = 1)
    Long id;

    @Column(name = "user_id")
    long userId;

    String title;

    @Column(name = "date_from")
    LocalDate dateFrom;

    @Column(name = "time_from")
    LocalTime timeFrom;

    @Column(name = "date_to")
    LocalDate dateTo;

    @Column(name = "time_to")
    LocalTime timeTo;

    BigDecimal payment;

    @Column(name = "service_id")
    long serviceId;

    @Column(name = "client_id")
    long clientId;

    @ManyToOne
    @JoinColumn(name = "service_id", insertable = false, updatable = false)
    Service service;

    @ManyToOne
    @JoinColumn(name = "client_id", insertable = false, updatable = false)
    Client client;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    User user;

    @Builder
    @SuppressWarnings("java:S107")
    public WorkRegister(String title, LocalDate dateFrom, LocalTime timeFrom, LocalDate dateTo, LocalTime timeTo, BigDecimal payment, long userId, long serviceId, long clientId) {
        this.title = title;
        this.dateFrom = dateFrom;
        this.timeFrom = timeFrom;
        this.dateTo = dateTo;
        this.timeTo = timeTo;
        this.payment = payment;
        this.userId = userId;
        this.serviceId = serviceId;
        this.clientId = clientId;
    }
}
