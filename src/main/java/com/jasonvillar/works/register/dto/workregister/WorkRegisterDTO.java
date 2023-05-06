package com.jasonvillar.works.register.dto.workregister;

import com.jasonvillar.works.register.dto.client.ClientDTO;
import com.jasonvillar.works.register.dto.service.ServiceDTO;
import com.jasonvillar.works.register.dto.user.UserDTO;

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
