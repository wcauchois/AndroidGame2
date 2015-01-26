package net.cloudhacking.androidgame2.engine.gl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import net.cloudhacking.androidgame2.engine.utils.Loggable;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by wcauchois on 1/22/15.
 */
public class TextRenderer extends Loggable {
    public static final int MAX_CACHE_SIZE = 200;
    //public static final long EXPIRATION_TIME_SEC = 30;

    public static final float DEFAULT_TEXT_SIZE = 60.0f;
    // Color is stored as a hex value, e.g. 0xFFFFFFFF (4 bytes = rgba)
    public static final int DEFAULT_TEXT_COLOR = Color.WHITE;
    public static final Typeface DEFAULT_TYPEFACE = Typeface.DEFAULT;

    public static TextProps newProps() {
        return new TextProps();
    }

    public static class TextProps {
        // may need to do text size in device independent units (dp) instead of pixels
        // so that scaling isn't weird on other screens? http://stackoverflow.com/a/3062023
        private float mTextSize = DEFAULT_TEXT_SIZE;
        private int mTextColor = DEFAULT_TEXT_COLOR;
        private Typeface mTypeface = DEFAULT_TYPEFACE;

        public TextProps textSize(float newTextSize) {
            mTextSize = newTextSize;
            return this;
        }

        public TextProps textColor(int newColor) {
            mTextColor = newColor;
            return this;
        }

        public TextProps typeface(Typeface newTypeface) {
            mTypeface = newTypeface;
            return this;
        }

        private TextProps() {}

        @Override public boolean equals(Object o) {
            if (!(o instanceof TextProps)) {
                return false;
            } else {
                TextProps other = (TextProps) o;
                return mTextSize == other.mTextSize &&
                        mTextColor == other.mTextColor &&
                        mTypeface.equals(other.mTypeface);
            }
        }

        @Override public int hashCode() {
            // http://developer.android.com/reference/java/lang/Object.html#writing_hashCode
            int result = 17;
            result = 31 * result + Float.floatToIntBits(mTextSize);
            result = 31 * result + mTextColor;
            result = 31 * result + mTypeface.hashCode();
            return result;
        }
    }

    private class StringAndProps {
        String s;
        TextProps props;

        StringAndProps(String s, TextProps props) {
            this.s = s;
            this.props = props;
        }

        @Override public boolean equals(Object o) {
            if (!(o instanceof StringAndProps)) {
                return false;
            } else {
                StringAndProps other = (StringAndProps) o;
                return s == other.s && props.equals(other.props);
            }
        }

        @Override public int hashCode() {
            int result = 84;
            result = 31 * result + s.hashCode();
            result = 31 * result + props.hashCode();
            return result;
        }
    }

    private LoadingCache<StringAndProps, Texture> mTextureCache;

    private class TextRemovalListener implements RemovalListener<StringAndProps, Texture> {
        @Override
        public void onRemoval(RemovalNotification<StringAndProps, Texture> notification) {
            // Delete the texture
            notification.getValue().delete();
        }
    }

    private class TextLoader extends CacheLoader<StringAndProps, Texture> {
        @Override
        public Texture load(StringAndProps sAndP) throws Exception {
            String s = sAndP.s;
            TextProps props = sAndP.props;

            Paint paint = new Paint();
            paint.setAntiAlias(true); // Only the crispest texts
            paint.setTextSize(props.mTextSize);
            paint.setTypeface(props.mTypeface);
            paint.setColor(props.mTextColor);

            Rect textBounds = new Rect();
            paint.getTextBounds(s, 0, s.length(), textBounds);
            Bitmap bitmap = Bitmap.createBitmap(
                    textBounds.width(), textBounds.height(), Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(bitmap);
            bitmap.eraseColor(0);
            canvas.drawText(s, -textBounds.left, -textBounds.top, paint);

            TextRenderer.this.d(String.format("generated %dx%d texture for \"%s\"",
                    bitmap.getWidth(), bitmap.getHeight(), s));

            return new Texture(bitmap);
        }
    }

    public TextRenderer() {
        mTextureCache = CacheBuilder.newBuilder()
                .maximumSize(MAX_CACHE_SIZE)
                //.expireAfterAccess(EXPIRATION_TIME_SEC, TimeUnit.SECONDS)
                //  ***for some reason, if the expiration access is set, the textures will turn
                //     black after the expiration time but not reload
                .removalListener(new TextRemovalListener())
                .build(new TextLoader());
    }

    public Texture getTexture(String s, TextProps props) {
        try {
            return mTextureCache.get(new StringAndProps(s, props));
        } catch (ExecutionException ex) {
            e("Failed to get texture", ex);
            throw new RuntimeException(ex);
        }
    }

    public void reloadTextures() {
        ConcurrentMap<StringAndProps, Texture> textureMap = mTextureCache.asMap();
        for (Texture tex : textureMap.values()) {
            tex.reload();
        }
    }

    private static TextRenderer sInstance = null;
    public static TextRenderer getInstance() {
        if (sInstance == null) {
            sInstance = new TextRenderer();
        }
        return sInstance;
    }
    public static void clearInstance() {
        sInstance = null;
    }
}
