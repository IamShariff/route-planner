package com.metro.routeplanner.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineIndex {

    private String lineName;
    private int index;
}

