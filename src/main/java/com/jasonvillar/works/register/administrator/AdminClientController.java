package com.jasonvillar.works.register.administrator;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/client")
@Validated
@RequiredArgsConstructor
public class AdminClientController {

}
