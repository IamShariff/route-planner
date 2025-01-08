package com.metro.routeplanner.util;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Component;

import com.metro.routeplanner.dao.StationDao;
import com.metro.routeplanner.exception.NotFoundException;
import com.metro.routeplanner.model.Station;
import com.metro.routeplanner.service.impl.MetroGraphJGraphT;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RouteUtil {

	private final MetroGraphJGraphT metroGraph;
	private final StationDao stationDao;

	// Calculate total stations between two stations using JGraphT graph
	public int getTotalStationsBetween(String sourceStationName, String destinationStationName) {
		Graph<String, DefaultWeightedEdge> graph = metroGraph.getGraph();

		if (graph == null) {
			throw new IllegalStateException("Graph has not been initialized.");
		}

		// Ensure vertices exist in the graph
		if (!graph.containsVertex(sourceStationName.toUpperCase())
				|| !graph.containsVertex(destinationStationName.toUpperCase())) {
			throw new NotFoundException(Constant.STATION, "Source or Destination station not found.");
		}

		// Use a shortest path algorithm
		DijkstraShortestPath<String, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(graph);
		GraphPath<String, DefaultWeightedEdge> path = dijkstra.getPath(sourceStationName.toUpperCase(),
				destinationStationName.toUpperCase());

		if (path == null) {
			throw new NotFoundException("Route", "No route found between the specified stations.");
		}

		// Return the number of edges as the total number of stations between (excluding
		// source and destination)
		return path.getLength();
	}

	// Find station by name or throw exception
	public Station findStationByName(String stationName) {
		return stationDao.findByName(stationName)
				.orElseThrow(() -> new NotFoundException(Constant.STATION, Constant.STATION_NOT_FOUND));
	}
	
	
}