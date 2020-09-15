package es.situm.capacitycontrol.geofences;

import androidx.annotation.NonNull;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.situm.sdk.model.cartography.Floor;
import es.situm.sdk.model.cartography.Geofence;
import es.situm.sdk.model.cartography.Point;

/**
 * Created by Regueira on 25/06/20
 */
public class GeofenceUtils {

    public final static String CUSTOM_FIELD_MAX_CAPACITY = "max_capacity";
    public final static String CUSTOM_FIELD_WARNING_CAPACITY_THRESHOLD = "warning_capacity_threshold";
    public final static Float WARNING_CAPACITY_PERCENTAGE_DEFAULT = 0.8F;

    private GeometryFactory geometryFactory = new GeometryFactory();

    public boolean checkIfCoordinateIsInsideGeofence(es.situm.sdk.model.location.Coordinate coordinate, Geofence geofence) {
        org.locationtech.jts.geom.Point point = geometryFactory.createPoint(new Coordinate(coordinate.getLatitude(), coordinate.getLongitude()));
        return point.within(geofenceToJtsPolygon(geofence));
    }

    public Map<Floor, List<Geofence>> createFloorGeofencesMap(@NonNull List<Floor> floors, List<Geofence> geofences) {
        Map<Floor, List<Geofence>> floorGeofencesMap = new HashMap<>();
        for (Floor floor : floors) {
            List<Geofence> geofenceList = new ArrayList<>();
            for (Geofence geofence : geofences) {
                if (geofence.getFloorIdentifier().equals(floor.getIdentifier())) {
                    geofenceList.add(geofence);
                }
            }
            floorGeofencesMap.put(floor, geofenceList);
        }
        return floorGeofencesMap;
    }

    private Polygon geofenceToJtsPolygon(Geofence geofence) {
        List<Coordinate> jtsCoordinates = new ArrayList<>();
        for (Point point : geofence.getPolygonPoints()) {
            Coordinate coordinate = new Coordinate(point.getCoordinate().getLatitude(), point.getCoordinate().getLongitude());
            jtsCoordinates.add(coordinate);
        }
        return geometryFactory.createPolygon(jtsCoordinates.toArray(new Coordinate[0]));
    }
}
