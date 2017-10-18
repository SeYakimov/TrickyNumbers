package com.airse.trickynumbers;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.airse.trickynumbers.TrickyNumbers;

public class IOSLauncher extends IOSApplication.Delegate implements IActivityRequestHandler  {
    private static IOSLauncher application;
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        if (application == null) {
            application = new IOSLauncher();
        }
        return new IOSApplication(new TrickyNumbers(application), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }

    @Override
    public void showAds(boolean show) {

    }
}