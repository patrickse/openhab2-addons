package org.openhab.binding.volumio2.internal;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONException;
import org.json.JSONObject;
import org.openhab.binding.volumio2.internal.mapping.Volumio2Commands;
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

        try {

            final BlockingQueue<Object> values = new LinkedBlockingQueue<Object>();
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    values.offer("done");
                }
            });

            socket.connect();
            values.take();

        } catch (InterruptedException ie) {
            logger.error("Timeout during connection");
        }
    }

    public void disconnect() {
        socket.disconnect();
    }

    public void addEventEmitter(String eventName, Emitter.Listener eventListener) {
        socket.on(eventName, eventListener);
    }

    public void getState() {
        socket.emit(Volumio2Commands.GET_STATE);
    }

    public void play() {
        socket.emit(Volumio2Commands.PLAY);
    }

    public void pause() {
        socket.emit(Volumio2Commands.PAUSE);
    }

    public void play(Integer index) {
        socket.emit(Volumio2Commands.PLAY, index);
    }

    public void next() {
        socket.emit(Volumio2Commands.NEXT);
    }

    public void previous() {
        socket.emit(Volumio2Commands.PREVIOUS);
    }

    public void setVolume(int level) {
        socket.emit(Volumio2Commands.VOLUME, level);
    }

    public void shutdown() {
        socket.emit(Volumio2Commands.SHUTDOWN);
    }

    public void reboot() {
        socket.emit(Volumio2Commands.REBOOT);
    }

    public void playPlaylist(String playlistName) {
        JSONObject item = new JSONObject();

        try {
            item.put("name", playlistName);

            socket.emit(Volumio2Commands.PLAY_PLAYLIST, item);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void playFavorites(String favoriteName) {
        JSONObject item = new JSONObject();

        try {
            item.put("name", favoriteName);

            socket.emit(Volumio2Commands.PLAY_FAVOURITES, item);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Play a radio station from volumio´s Radio Favourites identifed by
     * its index.
     *
     * @param index
     */
    public void playRadioFavourite(Integer index) {
        logger.debug("socket.emit({})", Volumio2Commands.PLAY_RADIO_FAVOURITES);

        socket.once("pushPlayRadioFavourites", new Emitter.Listener() {

            @Override
            public void call(Object... arg0) {
                play(index);
            }

        });

        socket.emit(Volumio2Commands.PLAY_RADIO_FAVOURITES);

    }

    public void addPlay(String uri, String title, String serviceType) {
        JSONObject item = new JSONObject();

        try {
            item.put("uri", uri);
            item.put("title", title);
            item.put("service", serviceType);

            socket.emit(Volumio2Commands.ADD_PLAY, item);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
