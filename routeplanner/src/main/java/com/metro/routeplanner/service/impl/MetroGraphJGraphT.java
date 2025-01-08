package com.metro.routeplanner.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Component;

import com.metro.routeplanner.dao.StationDao;
import com.metro.routeplanner.model.LineIndex;
import com.metro.routeplanner.model.Station;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MetroGraphJGraphT {

    private final StationDao stationDao;
    private Graph<String, DefaultWeightedEdge> graph;

    @PostConstruct
    public void buildGraph() {
        graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        List<Station> stations = stationDao.findAll();

        // Add all stations as vertices (using uppercase for consistency)
        for (Station station : stations) {
            graph.addVertex(station.getName().toUpperCase());
        }

        // Add edges based on lines
        Set<String> allLines = stations.stream()
                .flatMap(s -> s.getLineIndex().stream())
                .map(LineIndex::getLineName)
                .collect(Collectors.toSet());

        for (String lineName : allLines) {
            List<Station> stationsOnLine = stationDao.findAllByLineOrderByOrderIndex(lineName);
            for (int i = 0; i < stationsOnLine.size() - 1; i++) {
                String from = stationsOnLine.get(i).getName().toUpperCase();
                String to = stationsOnLine.get(i + 1).getName().toUpperCase();

                // Add edge from 'from' to 'to'
                DefaultWeightedEdge edge = graph.addEdge(from, to);
                if (edge != null) {
                    graph.setEdgeWeight(edge, 1); // Set weight as needed
                }

                // Add edge from 'to' to 'from' for bidirectional travel
                DefaultWeightedEdge reverseEdge = graph.addEdge(to, from);
                if (reverseEdge != null) {
                    graph.setEdgeWeight(reverseEdge, 1); // Set weight as needed
                }
            }
        }
    }

    public Graph<String, DefaultWeightedEdge> getGraph() {
        return graph;
    }
}
