package com.metro.routeplanner.model;

import java.util.List;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private boolean isInterchangable;

    @JdbcTypeCode(SqlTypes.JSON)
   	@Column(name = "line_info", columnDefinition = "jsonb")
    private List<LineIndex> lineIndex;


}
