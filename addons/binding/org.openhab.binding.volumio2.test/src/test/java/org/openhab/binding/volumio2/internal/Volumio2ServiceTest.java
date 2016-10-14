package org.openhab.binding.volumio2.internal;

import java.net.URISyntaxException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Volumio2ServiceTest {

    Volumio2Service service;

    @Before
    public void connect() throws URISyntaxException {
        service = new Volumio2Service("volumio.local");
        service.connect();
    }

    @After
    public void disconnect() {
        service.disconnect();
        service = null;
    }

    @Test
    public void getState() {
        service.getState();
    }

    @Test
    public void playRadioStation() {
        System.out.println("Start");
        service.playRadioFavourites();
        service.play(1);
        System.out.println("Stop");
    }

}
