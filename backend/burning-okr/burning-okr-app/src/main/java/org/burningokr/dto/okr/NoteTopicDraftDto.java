package org.burningokr.dto.okr;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteTopicDraftDto extends NoteDto {

    private Long parentTopicDraftId;

    public NoteTopicDraftDto(NoteDto noteDto) {
        super(noteDto);
    }

    public NoteTopicDraftDto() {
        super();
    }
}
