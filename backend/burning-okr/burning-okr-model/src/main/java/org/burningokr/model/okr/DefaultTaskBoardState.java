package org.burningokr.model.okr;

import lombok.Data;
import org.burningokr.model.activity.Trackable;

import javax.persistence.*;

@Entity
@Data
public class DefaultTaskBoardState implements Trackable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 255)
    private String title;

    @Override
    public String getName() {
        return title;
    }

    @Override
    public Long getId() {
        return id;
    }
}
