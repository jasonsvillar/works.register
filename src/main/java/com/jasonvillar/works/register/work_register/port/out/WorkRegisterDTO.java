package com.jasonvillar.works.register.work_register.port.out;

import com.jasonvillar.works.register.client.port.out.ClientDTO;
import com.jasonvillar.works.register.service.port.out.ServiceDTO;
import com.jasonvillar.works.register.user.port.out.UserDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record WorkRegisterDTO(
        long id,
        String title,
        LocalDate dateFrom,
        LocalTime timeFrom,
        LocalDate dateTo,
        LocalTime timeTo,
        BigDecimal payment,
        UserDTO user,
        ServiceDTO service,
        ClientDTO client
) {
}
