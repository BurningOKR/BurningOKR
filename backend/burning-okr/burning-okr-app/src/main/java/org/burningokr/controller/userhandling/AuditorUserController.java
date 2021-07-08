package org.burningokr.controller.userhandling;

import org.burningokr.service.userhandling.AuditorUserService;
import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestApiController
@RequiredArgsConstructor
public class AuditorUserController {

    private final AuditorUserService auditorUserService;
    /**
     * API Endpoint to check if the current user is an auditor.
     *
     * @return a {@link ResponseEntity} ok with a boolean
     */
    @GetMapping("/auditor/self")
    public ResponseEntity<Boolean> isCurrentUserAuditor() {
        return ResponseEntity.ok(auditorUserService.isCurrentUserAuditor());
    }
}
