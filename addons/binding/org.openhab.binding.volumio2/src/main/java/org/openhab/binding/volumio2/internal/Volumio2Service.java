package org.openhab.binding.volumio2.internal;

import java.net.URI;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Volumio2Service {

    private static final Logger logger = LoggerFactory.getLogger(Volumio2Service.class);

    private Socket socket;
    private String hostname;

    public Volumio2Service(String hostname) throws URISyntaxException {

        this.hostname = hostname;

        URI destUri = new URI("http://" + hostname);

        socket = IO.socket(destUri);
        bindDefaultEvents();
    }

    private void bindDefaultEvents() {
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... arg0) {
                logger.info("Connected to Volumio2 on {}", hostname);
            }

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... arg0) {
                logger.info("Disconnected from Volumio2 on {}", hostname);
            }
        });
    }

    public void connect() {
        socket.connect();
    }

    public void disconnect() {
        socket.disconnect();
    }

    public void addEventEmitter(String eventName, Emitter.Listener eventListener) {
        socket.on(eventName, eventListener);
    }

    public void getState() {
        socket.emit(VolumioServices.GET_STATE);
    }

    public void play() {
        socket.emit(VolumioServices.PLAY);
    }

    public void pause() {
        socket.emit(VolumioServices.PAUSE);
    }

    public void next() {
        socket.emit(VolumioServices.NEXT);
    }

    public void previous() {
        socket.emit(VolumioServices.PREVIOUS);
    }

    public void setVolume(int level) {
        socket.emit(VolumioServices.VOLUME, level);
    }

    public void shutdown() {
        socket.emit(VolumioServices.SHUTDOWN);
    }

    public void reboot() {
        socket.emit(VolumioServices.REBOOT);
    }

    public void playPlaylist(String playlistName) {
        JSONObject item = new JSONObject();

        try {
            item.put("name", playlistName);

            socket.emit(VolumioServices.PLAY_PLAYLIST, item);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void playFavorites(String favoriteName) {
        JSONObject item = new JSONObject();

        try {
            item.put("name", favoriteName);

            socket.emit(VolumioServices.PLAY_FAVOURITES, item);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void playRadioFavourites(String radioFavouriteName) {
        logger.debug("socket.emit({}, {})", VolumioServices.PLAY_RADIO_FAVOURITES, radioFavouriteName);
        socket.emit(VolumioServices.PLAY_RADIO_FAVOURITES, radioFavouriteName);
    }

    public void addPlay(String uri, String title, String serviceType) {
        JSONObject item = new JSONObject();

        try {
            item.put("uri", uri);
            item.put("title", title);
            item.put("service", serviceType);

            socket.emit(VolumioServices.ADD_PLAY, item);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
