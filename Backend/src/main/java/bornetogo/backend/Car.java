package main.java.bornetogo.backend;

import java.io.*;
import java.util.*;
import jakarta.json.*;
import java.sql.*;


// TODO: fill the empty fields!
// ISSUE! several wattages!


public class Car extends Entry
{
	private String model = "";
	private String subscription = "";
	private double maxAutonomy; // in km
	private double currentAutonomy; // in km
	private double maxWattage; // in kW
	private double capacity; // in kWh
	private ArrayList<String> connectors = new ArrayList<String>();
	private ArrayList<String> currents = new ArrayList<String>();

	// In reguards to the database:
	private int idCar;
	private int idBattery;


	public Car() {}


	// For testing. Real cars come from the database.
	public Car(String model, double maxAutonomy, double currentAutonomy, String subscription)
	{
		this.model = model;
		this.subscription = subscription;
		this.maxAutonomy = maxAutonomy;
		this.currentAutonomy = currentAutonomy;
	}


	public Car query(ResultSet answer)
	{
		Car car = new Car();

		try {
			car.idCar = answer.getInt("idVoiture");
			car.idBattery = answer.getInt("idBatterie");
			car.model = Entry.sanitize(answer.getString("Modele"));
			// 'Chargement' is useless.

			// String row = "-> " + car.idCar + ", " + car.idBattery + ", " + car.model;
			// System.out.println(row);

			return car;
		}
		catch (Exception e) {
			System.err.printf("\nInvalid fields in '%s' query.\n", this.getClass().getSimpleName());
			return null;
		}
	}


	public Car copy()
	{
		Car car = new Car();

		car.idCar = this.idCar;
		car.idBattery = this.idBattery;
		car.model = this.model;
		car.subscription = this.subscription;
		car.maxAutonomy = this.maxAutonomy;
		car.currentAutonomy = this.currentAutonomy;
		car.maxWattage = this.maxWattage;
		car.capacity = this.capacity;
		car.connectors = this.connectors;
		car.currents = this.currents;

		return car;
	}


	public int getId()
	{
		return this.idCar;
	}


	public int getIdBattery()
	{
		return this.idBattery;
	}


	public String getModel()
	{
		return this.model;
	}


	public String getSubscription()
	{
		return this.subscription;
	}


	public double getMaxAutonomy()
	{
		return this.maxAutonomy;
	}


	public double getCurrentAutonomy()
	{
		return this.currentAutonomy;
	}


	public double getMaxWattage()
	{
		return this.maxWattage;
	}


	public double getCapacity()
	{
		return this.capacity;
	}


	public ArrayList<String> getConnectors()
	{
		return this.connectors;
	}


	public ArrayList<String> getCurrents()
	{
		return this.currents;
	}


	public void setMaxWattage(PowerConnector pc)
	{
		this.maxWattage = pc.getWattage();
	}


	public void setCapacity(Battery battery)
	{
		this.capacity = battery.getCapacity();
	}


	public void setMaxAutonomy(Battery battery)
	{
		this.maxAutonomy = battery.getAutonomy();
		this.currentAutonomy = this.maxAutonomy; // by default
	}


	public void setCurrentAutonomy(double autonomy)
	{
		this.currentAutonomy = Math.max(0., Math.min(autonomy, this.maxAutonomy));
	}


	public String toString()
	{
		return "Car: " + this.model + "\nSubscription: " + this.subscription + "\nMax autonomy: " + this.maxAutonomy +
			" km\nCurrent autonomy: " + this.currentAutonomy + " km\nmaxWattage: " + this.maxWattage +
			" kW\nCapacity: " + this.capacity + " kWh\nConnectors number: " + this.connectors.size() +
			"\nCurrents number: " + this.currents.size();
	}


	public void print()
	{
		System.out.println(this.toString());
	}


	// Returns null on failure.
	public static Car getFromJson(JsonObject json)
	{
		try
		{
			JsonObject carJson = json.getJsonObject("car");
			String model = carJson.getString("model");
			String subscription = carJson.getString("subscription");
			double maxAutonomy = carJson.getJsonNumber​("maxAutonomy").doubleValue(); // in km
			double currentAutonomy = carJson.getJsonNumber​("currentAutonomy").doubleValue(); // in km

			// TODO: update the constructor to add those:
			double maxWattage = carJson.getJsonNumber​("maxWattage").doubleValue(); // in kW
			double capacity = carJson.getJsonNumber​("capacity").doubleValue(); // in kWh
			// ArrayList<String> connectors = carJson.getString("connectors"); // TODO
			// ArrayList<String> currents = carJson.getString("currents"); // TODO

			return new Car(model, maxAutonomy, currentAutonomy, subscription);
		}
		catch (Exception e) {
			System.err.println("\nError while parsing a json: could not extract a car.\n");
			return null;
		}
	}


	public JsonObject toJson()
	{
		JsonArrayBuilder connectorsBuilder = Json.createArrayBuilder();

		for (String connector : this.connectors) {
			connectorsBuilder.add(connector);
		}

		JsonArray connectorsArray = connectorsBuilder.build();


		JsonArrayBuilder currentsBuilder = Json.createArrayBuilder();

		for (String current : this.currents) {
			currentsBuilder.add(current);
		}

		JsonArray currentsArray = currentsBuilder.build();


		return Json.createObjectBuilder()
			.add("model", this.model)
			.add("subscription", this.subscription)
			.add("maxAutonomy", this.maxAutonomy)
			.add("currentAutonomy", this.currentAutonomy)
			.add("maxWattage", this.maxWattage)
			.add("capacity", this.capacity)
			.add("connectors", connectorsArray)
			.add("currents", currentsArray)
			.build();
	}


	// For testing only:
	public static ArrayList<Car> mock()
	{
		ArrayList<Car> allCars = new ArrayList<Car>();
		allCars.add(new Car("Renault Zoe", 66, 66, ""));
		allCars.add(new Car("Tesla cybertruck", 200, 200, ""));
		return allCars;
	}


	public static void main(String[] args)
	{
		Car car_1 = new Car("Renault Zoe", 66, 30, "None");
		System.out.println(car_1.toString() + "\n");

		String inputString = FileContent.read("input_example_singleStep.json");
		JsonObject input = GetJson.jsonFromString(inputString);
		Car car_2 = getFromJson(input);
		System.out.println(car_2.toString() + "\n");
	}
}

// {
//     model: "Renault ZOE R135"
//     subscription: ""
//     maxAutonomy: 390
//     currentAutonomy: 390
//     capacity: 52
//     currentConnectors: [
//         {
//             wattage: 150.0,
//             connector: "",
//             current: ""
//         },
//         {
//             wattage: 150.0,
//             connector: "",
//             current: ""
//         }
//     ]
// }
