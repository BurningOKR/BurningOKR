package org.burningokr.model.okr;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.burningokr.model.activity.Trackable;
import org.burningokr.model.okrUnits.OkrUnit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
public class TaskBoard implements Trackable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "parent_unit_id")
    private OkrUnit parentOkrUnit;

    @OneToMany(mappedBy = "parentTaskBoard")
    @EqualsAndHashCode.Exclude
    private Collection<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "parentTaskBoard")
    private Collection<TaskState> availableStates = new ArrayList<>();

    @Override
    public String getName() {
        return "";
    }

    @Override
    public Long getId() {
        return id;
    }
}
