package org.openhab.binding.volumio2.internal.json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.smarthome.core.library.types.RawType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VolumioJsonObject extends JSONObject {

    private static final Logger logger = LoggerFactory.getLogger(VolumioJsonObject.class);

    public VolumioJsonObject() {
        super();
    }

    public VolumioJsonObject(JSONObject object) throws JSONException {
        super(object.toString());
    }

    public StringType getStringType(String param) {

        try {
            String objStr = this.getString(param);

            if (this.isNull(param)) {
                objStr = "";
            }

            logger.debug("getStringType(\"{}\"): {}", param, objStr);

            return new StringType(objStr);
        } catch (JSONException e) {
            return null;
        }
    }

    public RawType getRawType(String param) throws JSONException {

        String imageUrl;

        if (this.getString(param).startsWith("http")) {
            imageUrl = String.format("%s", this.getString(param));
        } else {
            imageUrl = String.format("http://volumio.local%s", this.getString(param));
        }

        ByteArrayOutputStream baos = null;
        RawType rawType = null;

        try {
            baos = readImageFromURL(new URL(imageUrl));
            rawType = new RawType(baos.toByteArray());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rawType;

    }

    private ByteArrayOutputStream readImageFromURL(URL url) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            is = url.openStream();
            byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
            int n;

            while ((n = is.read(byteChunk)) > 0) {
                baos.write(byteChunk, 0, n);
            }
        } catch (IOException e) {
            System.err.printf("Failed while reading bytes from %s: %s", url.toExternalForm(), e.getMessage());
            e.printStackTrace();
            // Perform any other exception handling that's appropriate.
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return baos;

    }

}
