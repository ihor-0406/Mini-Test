package com.example.authapi.controller;

import com.example.authapi.dto.AuthDto;
import com.example.authapi.model.ProcessingLog;
import com.example.authapi.repository.ProcessingLogRepository;
import com.example.authapi.repository.UserRepository;
import com.example.authapi.service.DataApiClient;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProcessController {

    private final UserRepository users;
    private final ProcessingLogRepository logs;
    private final DataApiClient data;

    public ProcessController(UserRepository users, ProcessingLogRepository logs, DataApiClient data) {
        this.users = users;
        this.logs = logs;
        this.data = data;
    }

    @PostMapping("/process")
    public Map<String, String> process(@Valid @RequestBody AuthDto.ProcessRequest req,
                                       Authentication authentication) {

        var email = (String) authentication.getPrincipal();
        var user = users.findByEmail(email).orElse(null);
        var out = data.transform(req.text());

        logs.save(new ProcessingLog(user.getId(), req.text(), out));
        return Map.of("result", out);
    }
}
