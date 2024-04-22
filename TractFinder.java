import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.net.URI;

public class TractFinder
{
  public static void main(String [] args)
  {
    ArrayList<String> tracts = new ArrayList<String>();
    ArrayList<Stop> stops = new ArrayList<Stop>();

    try
    {
      Scanner s = new Scanner(new File("gtfs/new-stops.txt"));
      int z = 0; // skip first
      int stopCount = 0;
      while (s.hasNextLine())
      {
        if (z == 0)
        {
          s.nextLine();
          z++;
        }
        else
        {
          String data = s.nextLine();
          String stopID = data.substring(0, data.indexOf(","));
          data = data.substring(data.indexOf(",") + 1);
          String stopName = data.substring(0, data.indexOf(","));
          data = data.substring(data.indexOf(",") + 1);
          double stopLat = Double.parseDouble(data.substring(0, data.indexOf(",")));
          data = data.substring(data.indexOf(",") + 1);
          double stopLon = Double.parseDouble(data.substring(0, data.indexOf(",")));
          stops.add(new Stop(stopID, stopName, stopLat, stopLon));
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error - no stops.txt.");
    }

    for (int i = 0; i < stops.size(); i++)
    {
      try
      {
        Scanner read = new Scanner(new URI("https://geocoding.geo.census.gov/geocoder/geographies/coordinates?x=" + stops.get(i).getLon() + "&y=" + stops.get(i).getLat() + "&benchmark=Public_AR_Census2020&vintage=Census2020_Census2020&format=json").toURL().openStream());

        while (read.hasNextLine())
        {
          String data = read.nextLine();
          data = data.substring(data.indexOf("\"Census Tracts\"")); // truncate to tract data

          data = data.substring(data.indexOf("\"TRACT\":")); // move to tract number
          tracts.add(data.substring(9, data.indexOf(",") - 1));
        }

        System.out.println("Stops: " + (i + 1) + " / " + stops.size());
      }
      catch (Exception e)
      { /* nothing */ }
    }

    try
    {
      File tractFile = new File("gtfs/new-tract.txt");
      FileWriter tractWriter = new FileWriter(tractFile);

      tractWriter.write("stop_id,tract\n");

      for (int i = 0; i < tracts.size(); i++)
      {
        tractWriter.append(stops.get(i).getStopID() + "," + tracts.get(i) + "\n");
      }
      tractWriter.close();
      System.out.println("Tract File Written.");
    }
    catch (Exception e)
    {
      System.out.println("Error - can't write tracts.");
    }
  }
}