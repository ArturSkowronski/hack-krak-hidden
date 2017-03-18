package tech.kolektiv.sdm.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tech.kolektiv.sdm.R;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class DialogMapFragment extends DialogFragment {

    private SupportMapFragment fragment;
    List<Marker> positions = new ArrayList<>();
    private LatLng latLng;
    private String name;


    public DialogMapFragment() {
        fragment = new SupportMapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mapdialog, container, false);
        getDialog().setTitle("");
        ImageView button = (ImageView) view.findViewById(R.id.xbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogMapFragment.this.dismiss();
            }
        });
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.mapView, fragment).commit();
        if (latLng != null && name != null) printMarker(latLng, name);
        return view;
    }

    public void setMarker(LatLng latLng, String name) {
        this.latLng = latLng;
        this.name = name;

        printMarker(latLng, name);

    }

    private void printMarker(LatLng latLng, String name) {
        fragment.getMapAsync(googleMap -> {
            if (checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.clear();
            googleMap.setMyLocationEnabled(true);
            for (Marker position : positions) {
                position.remove();
            }
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(name);
            Marker marker = googleMap.addMarker(markerOptions);
            positions.add(marker);
            LatLngBounds.Builder b = new LatLngBounds.Builder();
            for (Marker m : positions) {
                b.include(m.getPosition());
            }

            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(latLng, 12.0f);
            googleMap.moveCamera(cu);
        });
    }
}