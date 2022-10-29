package top.meyok.user.shiro.other;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;

import java.io.File;
import java.io.InputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

/**
 * @author meyok@mbulletin.meyok.top
 * @date 2022/9/2 17:25
 */
public class SerializeSimpleByteSource implements ByteSource, Serializable {


    @Serial
    private static final long serialVersionUID = 6102897494201110628L;

    private final byte[] bytes;
    private String cachedHex;
    private String cachedBase64;

    public SerializeSimpleByteSource(byte[] bytes) {
        this.bytes = bytes;
    }

    public SerializeSimpleByteSource(char[] chars) {
        this.bytes = CodecSupport.toBytes(chars);
    }

    public SerializeSimpleByteSource(String string) {
        this.bytes = CodecSupport.toBytes(string);
    }

    public SerializeSimpleByteSource(ByteSource source) {
        this.bytes = source.getBytes();
    }

    public SerializeSimpleByteSource(File file) {
        this.bytes = (new SerializeSimpleByteSource.BytesHelper()).getBytes(file);
    }

    public SerializeSimpleByteSource(InputStream stream) {
        this.bytes = (new SerializeSimpleByteSource.BytesHelper()).getBytes(stream);
    }

    public static boolean isCompatible(Object o) {
        return o instanceof byte[] || o instanceof char[] || o instanceof String || o instanceof ByteSource || o instanceof File || o instanceof InputStream;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public boolean isEmpty() {
        return this.bytes == null || this.bytes.length == 0;
    }

    public String toHex() {
        if (this.cachedHex == null) {
            this.cachedHex = Hex.encodeToString(this.getBytes());
        }

        return this.cachedHex;
    }

    public String toBase64() {
        if (this.cachedBase64 == null) {
            this.cachedBase64 = Base64.encodeToString(this.getBytes());
        }

        return this.cachedBase64;
    }

    public String toString() {
        return this.toBase64();
    }

    public int hashCode() {
        return this.bytes != null && this.bytes.length != 0 ? Arrays.hashCode(this.bytes) : 0;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof ByteSource) {
            ByteSource bs = (ByteSource)o;
            return Arrays.equals(this.getBytes(), bs.getBytes());
        } else {
            return false;
        }
    }

    private static final class BytesHelper extends CodecSupport {
        private BytesHelper() {
        }

        public byte[] getBytes(File file) {
            return this.toBytes(file);
        }

        public byte[] getBytes(InputStream stream) {
            return this.toBytes(stream);
        }
    }

}
