package org.burningokr.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoteAuthorizationService {
  public boolean isOwner(Long noteId) {
    return true;
  }
}
