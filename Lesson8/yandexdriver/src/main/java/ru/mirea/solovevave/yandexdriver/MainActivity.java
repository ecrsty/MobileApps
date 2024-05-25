package ru.mirea.solovevave.yandexdriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.directions.DirectionsFactory;
import com.yandex.mapkit.directions.driving.DrivingOptions;
import com.yandex.mapkit.directions.driving.DrivingRoute;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.directions.driving.DrivingSession;
import com.yandex.mapkit.directions.driving.VehicleOptions;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.solovevave.yandexdriver.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements UserLocationObjectListener,
        DrivingSession.DrivingRouteListener {
    private ActivityMainBinding binding;
    private MapView mapView;
    private UserLocationLayer userLocationLayer;

    static final int REQUEST_CODE_PERMISSION = 200;
    boolean is_permissions_granted = false;

    private static final String TAG = "MainActivity";
    private static final Point START_POINT = new Point(55.670005, 37.479894);
    private static final Point END_POINT = new Point(55.794229, 37.700772);
    private static final Point CAMERA_TARGET = new Point(
            (START_POINT.getLatitude() + END_POINT.getLatitude()) / 2,
            (START_POINT.getLongitude() + END_POINT.getLongitude()) / 2);
    private DrivingRouter routeRouter;
    private MapObjectCollection mapObjects;
    private DrivingSession routeSession;
    private int[] routeColors = {0xFFFF0000, 0xFF00FF00, 0x00FFBBBB, 0xFF0000FF};
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MapKitFactory.initialize(this);
        DirectionsFactory.initialize(this);

        mapView = binding.mapview;
        mapView.getMap().move(new CameraPosition(CAMERA_TARGET, 10, 0, 0));
        routeRouter = DirectionsFactory.getInstance().createDrivingRouter();
        mapObjects = mapView.getMap().getMapObjects().addCollection();

        initializeMarker(new Point( 55.7935328, 37.7012789), "Стромынка, 20");

        checkPermissions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }

    private void initializeMarker(Point location, String message) {
        PlacemarkMapObject marker = mapView.getMap().getMapObjects().addPlacemark(location);
        marker.setIcon(ImageProvider.fromResource(this, com.yandex.maps.mobile.R.drawable.search_layer_pin_selected_default));
        marker.addTapListener((MapObject mapObject, Point point) -> {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            return false;
        });
    }

    private void requestRoute(Point startPoint, Point endPoint) {
        DrivingOptions options = new DrivingOptions();
        VehicleOptions vehicleOptions = new VehicleOptions();
        options.setRoutesCount(4);

        List<RequestPoint> points = new ArrayList<>();
        points.add(new RequestPoint(startPoint, RequestPointType.WAYPOINT, null));
        points.add(new RequestPoint(endPoint, RequestPointType.WAYPOINT, null));

        progressDialog = ProgressDialog.show(this, "Route Search", "Please wait...", true);
        routeSession = routeRouter.requestRoutes(points, options, vehicleOptions, this);
    }

    @Override
    public void onDrivingRoutes(@NonNull List<DrivingRoute> list) {
        for (int i = 0; i < list.size(); i++) {
            mapObjects.addPolyline(list.get(i).getGeometry()).setStrokeColor(routeColors[i]);
        }
        progressDialog.dismiss();
    }

    @Override
    public void onDrivingRoutesError(@NonNull Error error) {
        String errorMsg = "An error occurred";
        if (error instanceof RemoteError) {
            errorMsg = "Remote error";
        } else if (error instanceof NetworkError) {
            errorMsg = "Network error";
        }
        progressDialog.dismiss();
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onObjectAdded(@NonNull UserLocationView userLocationView) {
        userLocationView.getArrow().setIcon(ImageProvider.fromResource(this, android.R.drawable.arrow_up_float));
        CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();
        pinIcon.setIcon("pin",
                ImageProvider.fromResource(this, R.drawable.ic_launcher_foreground),
                new IconStyle().setAnchor(new PointF(0.5f, 0.5f))
                        .setRotationType(RotationType.ROTATE)
                        .setZIndex(1f)
                        .setScale(0.5f)
        );
        userLocationView.getAccuracyCircle().setFillColor(Color.BLUE & 0x99ffffff);

        MapKitFactory.getInstance().createLocationManager().requestSingleUpdate(new LocationListener() {
            @Override
            public void onLocationUpdated(@NonNull Location location) {
                Log.d("LocationUpdate", "Received location update");
                requestRoute(location.getPosition(), new Point(55.704205, 37.507699));
            }

            @Override
            public void onLocationStatusUpdated(@NonNull LocationStatus status) {}
        });
    }

    @Override
    public void onObjectRemoved(@NonNull UserLocationView userLocationView) {

    }

    @Override
    public void onObjectUpdated(@NonNull UserLocationView userLocationView, @NonNull ObjectEvent objectEvent) {

    }

    private void loadUserLocationLayer(){
        MapKit mapKit = MapKitFactory.getInstance();
        mapKit.resetLocationManagerToDefault();
        userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);
        userLocationLayer.setObjectListener(this);
    }

    private void checkPermissions() {
        boolean internetPermissionStatus = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
        boolean bgLocationPermissionStatus = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean coarseLocationPermissionStatus = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean fineLocationPermissionStatus = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        is_permissions_granted = internetPermissionStatus && bgLocationPermissionStatus &&
                coarseLocationPermissionStatus && fineLocationPermissionStatus;
        if(is_permissions_granted) {
            loadUserLocationLayer();
        } else {
            ActivityCompat.requestPermissions(this,
                    new	String[] { android.Manifest.permission.INTERNET,
                            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("", "onRequestPermissionsResult: " + String.valueOf(requestCode));
        if(requestCode == REQUEST_CODE_PERMISSION) {
            Log.d(TAG, permissions.toString());
            Log.d(TAG, grantResults.toString());
            is_permissions_granted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
            if(is_permissions_granted) {
                loadUserLocationLayer();
            }
        } else {
            finish();
        }
    }
}