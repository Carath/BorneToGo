package main.java.bornetogo.backend;

import java.util.*;
import jakarta.json.*;


public class Output
{
	// Returns null on failure.
	public static JsonObject build(ArrayList<Route> routes, long startTime)
	{
		try
		{
			// routesArray:

			JsonArrayBuilder routesBuilder = Json.createArrayBuilder();

			for (Route route : routes)
			{
				// Stats:

				JsonObject statsJson = Json.createObjectBuilder().build(); // TODO! Expand route.getStats()

				// waypoints:

				JsonArrayBuilder waypointsBuilder = Json.createArrayBuilder();

				for (Coord coord : route.getWaypoints()) {
					waypointsBuilder.add(coord.toJsonFull(Coord.Format.LONG_LAT));
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

				// fullPathJson:

				JsonObject fullPathJson = Json.createObjectBuilder()
					.add("length", route.getLength())
					.add("duration", route.getDuration())
					.add("cost", route.getCost())
					.add("autonomyLeft", route.getAutonomyLeft())
					.add("legs", legsArray)
					.add("geometry", route.getGeometry())
					.build();

				// routeJson:

				JsonObject routeJson = Json.createObjectBuilder()
					.add("stats", statsJson)
					.add("waypoints", waypointsArray)
					.add("fullPath", fullPathJson)
					.build();

				routesBuilder.add(routeJson);
			}

			JsonArray routesArray = routesBuilder.build();

			// output:

			long endTime = System.nanoTime();
			double processingTime = (endTime - startTime) / 1e9; // in seconds.

			JsonObject output = Json.createObjectBuilder()
				.add("type", "output")
				.add("status", "Ok")
				.add("processingTime", processingTime)
				.add("convention", "long-lat")
				.add("routes", routesArray)
				.build();

			return output;
		}
		catch (Exception e) {
			System.err.println("\nError while building the output json.\n");
			return null;
		}
	}
}
