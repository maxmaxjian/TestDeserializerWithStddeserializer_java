import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.File;
import java.io.IOException;

public class Item {
    private final String mDrawable;
    private final String mGravity;
    private final boolean mAntialias;
    private final String mTint;

    public Item(final String drawable, final String gravity,
                     final boolean antialis, final String tint)
    {
        mDrawable = drawable;
        mGravity = gravity;
        mAntialias = antialis;
        mTint = tint;
    }

    public String getDrawable() {
        return mDrawable;
    }

    public String getGravity() {
        return mGravity;
    }

    public boolean getAntialias() {
        return mAntialias;
    }

    public String getTint() {
        return mTint;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("src: ")
                .append(mDrawable+"\n")
                .append("gravity: ")
                .append(mGravity+"\n")
                .append("antialias: ")
                .append(mAntialias+"\n")
                .append("tint: ")
                .append(mTint);
        return sb.toString();
    }

    public static class Deserializer extends StdDeserializer<Item> {

        public Deserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Item deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            ObjectCodec codec = p.getCodec();
            JsonNode rootnode = codec.readTree(p);
//            System.out.println("rootnode = " + rootnode.toString());
            JsonNode node = rootnode.get("item");
//            System.out.println("node = " + node.toString());
            String drawable = node.get("android:src").asText();
            String gravity = node.get("android:gravity").asText();
            boolean antialias = node.get("android:antialias").asBoolean();
            String tint = node.get("android:tint").asText();
            return new Item(drawable, gravity, antialias, tint);
        }

    }

    public static void main(String[] args) {
        SimpleModule mod = new SimpleModule();
        mod.addDeserializer(Item.class, new Deserializer(Item.class));

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(mod);

        File file = new File("test.json");
        Item[] items = null;
        try {
            items = mapper.readValue(file, Item[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (items != null) {
            for (int i = 0; i < items.length; i++) {
                System.out.println(items[i]);
            }
        }
    }
}
