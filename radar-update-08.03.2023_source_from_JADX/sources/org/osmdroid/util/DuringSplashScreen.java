package org.osmdroid.util;

import org.osmdroid.tileprovider.modules.SqlTileWriter;

public class DuringSplashScreen implements SplashScreenable {
    public void runDuringSplashScreen() {
        new SqlTileWriter().runDuringSplashScreen();
    }
}
