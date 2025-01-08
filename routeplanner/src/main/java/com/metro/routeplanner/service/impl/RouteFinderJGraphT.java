package com.metro.routeplanner.service.impl;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.YenKShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RouteFinderJGraphT {

	private final MetroGraphJGraphT metroGraphJGraphT;

	/**
	 * Finds the K shortest paths between source and destination using JGraphT's
	 * Yen's Algorithm.
	 *
	 * @param source      Name of the source station.
	 * @param destination Name of the destination station.
	 * @param K           Number of paths to find.
	 * @return List of paths, where each path is a list of station names.
	 */
	public List<List<String>> findKShortestPaths(String source, String destination, int routesToRetrieve) {
		Graph<String, DefaultWeightedEdge> graph = metroGraphJGraphT.getGraph();

		if (graph == null) {
			throw new IllegalStateException("Graph has not been initialized.");
		}

		YenKShortestPath<String, DefaultWeightedEdge> kPathsAlg = new YenKShortestPath<>(graph);
		return kPathsAlg.getPaths(source.toUpperCase(), destination.toUpperCase(), routesToRetrieve).stream()
				.map(GraphPath::getVertexList).toList();
	}
}
