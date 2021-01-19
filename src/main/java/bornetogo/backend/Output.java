package main.java.bornetogo.backend;

import java.util.*;
import jakarta.json.*;


public class Output
{
	public static JsonObject buildAnswer(ArrayList<Route> routes, long startTime)
	{
		// routesArray:

		JsonArrayBuilder routesBuilder = Json.createArrayBuilder();

		for (Route route : routes)
		{
			// waypoints:

			JsonArrayBuilder waypointsBuilder = Json.createArrayBuilder();

			for (Coord coord : route.getWaypoints()) {
				waypointsBuilder.add(coord.toJsonFull(Coord.Format.LAT_LONG));
			}

			JsonArray waypointsArray = waypointsBuilder.build();

			// legs:

			JsonArrayBuilder legsBuilder = Json.createArrayBuilder();

			for (int i = 0; i < route.getLegsLengths().size(); ++i)
			{
				JsonObject legJson = Json.createObjectBuilder()
					.add("length", route.getLegsLengths().get(i))
					.add("duration", route.getLegsDurations().get(i))
					.build();

				legsBuilder.add(legJson);
			}

			JsonArray legsArray = legsBuilder.build();

			// coordinates:

			JsonArrayBuilder coordsBuilder = Json.createArrayBuilder();

			for (Coord coord : route.getFullPath()) {
				coordsBuilder.add(coord.toJsonSmall(Coord.Format.LAT_LONG));
			}

			JsonArray coordsArray = coordsBuilder.build();

			// geometry:

			JsonObject geometry = Json.createObjectBuilder()
				.add("coordinates", coordsArray)
				.build();

			// fullPathJson:

			JsonObject fullPathJson = Json.createObjectBuilder()
				.add("length", route.getLength())
				.add("duration", route.getDuration())
				.add("cost", route.getCost())
				.add("autonomyLeft", route.getAutonomyLeft())
				.add("stats", route.getStats())
				.add("legs", legsArray)
				.add("geometry", geometry)
				.build();

			// routeJson:

			JsonObject routeJson = Json.createObjectBuilder()
				.add("waypoints", waypointsArray)
				.add("fullPath", fullPathJson)
				.build();

			routesBuilder.add(routeJson);
		}

		JsonArray routesArray = routesBuilder.build();

		// answer:

		long endTime = System.nanoTime();
		double processingTime = (endTime - startTime) / 1e9;

		JsonObject answer = Json.createObjectBuilder()
			.add("type", "output")
			.add("status", "Ok")
			.add("processingTime", processingTime)
			.add("convention", "lat-long")
			.add("routes", routesArray)
			.build();

		return answer;
	}
}
