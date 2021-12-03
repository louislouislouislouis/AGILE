package org.hexanome.vue;

import com.gluonhq.maps.MapView;

public class CustomMap extends MapView {
    public CustomMap() {
        super();
    }

    public void supraUpdate() {
        layout();
    }

}
