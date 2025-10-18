package com.example.data.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class TransformController {

    @Value("${app.internal-token}")
    private String internalToken;

    public record TransformRequest(@NotBlank String text) {}
    public  record TransformResponse(@NotBlank String result) {}

    @PostMapping("/transform")
    public TransformResponse transform(@RequestHeader(value = "X-Internal-Token", required = false) String token,
                                       @Valid @RequestBody TransformRequest req) {
        if(token == null || !token.equals(internalToken)){
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid token");
        }

        var out = new StringBuffer(req.text().toUpperCase()).reverse().toString();
        return new TransformResponse(out);
    }
}
