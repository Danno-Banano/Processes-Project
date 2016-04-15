package processes_project.lootandrun;

import android.location.Location;
import android.os.SystemClock;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by trevor on 4/12/16.
 */
public class Manager {
    private MainMap map;
    private HashMap<Integer, MapObject<Character>> monsters;
    private HashMap<Integer, MapObject<Item>> items;
    private int maxNumMonsters;
    private int maxNumItems;
    private double loadRadius;
    private long refreshTimer;

    public Manager(MainMap map, final int maxNumMonsters, int maxNumItems, double loadRadius, int refreshTimer) {
        this.map = map;
        monsters = new HashMap<>();
        items = new HashMap<>();
        this.maxNumMonsters = maxNumMonsters;
        this.maxNumItems = maxNumItems;
        this.loadRadius = loadRadius;
        this.refreshTimer = refreshTimer;
    }

    public void startManager() {
        Thread t = new Thread() {
            public void run() {
                while (true) {
                    try {
                        sleep(refreshTimer);
                        map.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (monsters.size() < maxNumMonsters) {
                                    placeNewMonster();
                                }
                                if (items.size() < maxNumItems) {
                                    placeNewItem();
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        t.start();
    }

    private <E> Marker placeObjectOnMap(E object) {
        if (MainMap.getMainPlayer().getCharLocation() == null) {
            return null;
        }

        LatLng objLatLng =
                new LatLng(MainMap.getMainPlayer().getCharLocation().getLatitude() + randomValWithinLoadRadius()
                        , MainMap.getMainPlayer().getCharLocation().getLongitude() + randomValWithinLoadRadius());

        Marker objMarker = map.getGoogleMap().addMarker(new MarkerOptions()
                .position(objLatLng)
                .title(object.toString()));

        return objMarker;
    }

    public Character placeNewMonster() {
        Character newMonster = new Character();
        Marker newMonsterLocation = placeObjectOnMap(newMonster);
        monsters.put(newMonsterLocation.hashCode(), new MapObject<>(newMonster, newMonsterLocation));
        if (newMonsterLocation != null && monsters.size() < maxNumMonsters) {
            randomWalk(newMonsterLocation);
        }
        else {
            newMonsterLocation.remove();
            monsters.remove(newMonsterLocation);
        }

        return null;
    }

    public Item placeNewItem() {
        Item newItem = new Item("ManagerTest", 10, "Weapon");
        Marker newItemLocation = placeObjectOnMap(newItem);
        items.put(newItem.hashCode(), new MapObject<>(newItem, newItemLocation));
        if (!(newItemLocation != null && items.size() < maxNumItems)) {
            newItemLocation.remove();
            items.remove(newItemLocation.hashCode());
        }

        return null;
    }

    public void randomWalk(final Marker monsterMarker) {
        Thread t = new Thread() {
            public void run() {
                while (true) {
                    try {
                        sleep(100);
                        map.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LatLng charLatLng = new LatLng(MainMap.getMainPlayer().getCharLocation().getLatitude(), MainMap.getMainPlayer().getCharLocation().getLongitude());
                                float[] results = new float[1];
                                Location.distanceBetween(charLatLng.latitude, charLatLng.longitude, monsterMarker.getPosition().latitude, monsterMarker.getPosition().longitude, results);
                                if (results[0] >= 0.0001) {
                                    //map.fightMonster();
                                }

                                long start = SystemClock.currentThreadTimeMillis();
                                long elapsed = SystemClock.uptimeMillis() - start;
                                double interp = new LinearInterpolator().getInterpolation((float) elapsed / 500);

                                LatLng newPos = new LatLng(
                                        monsterMarker.getPosition().latitude + (0.00005 * (new Random().nextBoolean() ? 1 : -1))
                                        , monsterMarker.getPosition().longitude + (0.00005 * (new Random().nextBoolean() ? 1 : -1)));

                                double lat = interp * newPos.latitude + (1-interp) * monsterMarker.getPosition().latitude;
                                double lng = interp * newPos.longitude + (1-interp) * monsterMarker.getPosition().longitude;

                                LatLng intermediatePos = new LatLng(lat, lng);
                                monsterMarker.setPosition(newPos);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        t.start();
    }

    public double randomValWithinLoadRadius() {
        return (new Random().nextDouble()) * loadRadius * (new Random().nextBoolean() ? 1 : -1);
    }
/*
    public boolean detectProximityToMapObj(float threshold, MapObject mapObj) {
        LatLng charLatLng = new LatLng(MainMap.getMainPlayer().getCharLocation().getLatitude(), MainMap.getMainPlayer().getCharLocation().getLongitude());
        float[] results = new float[1];
        Location.distanceBetween(charLatLng.latitude, charLatLng.longitude, mapObj.objMarker.getPosition().latitude, mapObj.objMarker.getPosition().longitude, results);
        if (results[0] >= threshold) {
            interactWithMapObj(mapObj);
            return true;
        }

        return false;
    }
*/
    private class MapObject <E> {
        public E mapObj;
        public Marker objMarker;

        public MapObject(E mapObj, Marker objMarker) {
            this.mapObj = mapObj;
            this.objMarker = objMarker;
        }
    }
}
