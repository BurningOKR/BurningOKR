package org.burningokr.model.okr;
import org.burningokr.model.okrUnits.OkrUnit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class OkrTopicDraft extends OkrTopicDescription{

    @ManyToOne
    private OkrUnit parentUnit;

}
