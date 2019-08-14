import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LayerItem {
    private final String mDrawable;
    private final String mGravity;
    private final boolean mAntialias;
    private final String mTint;

    public LayerItem(final String drawable, final String gravity,
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

    public static class Deserializer extends StdDeserializer<LayerItem> {


        public Deserializer(Class<LayerItem> item) {
            super(item);
        }

        @Override
        public LayerItem deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            ObjectCodec codec = p.getCodec();
            JsonNode node = codec.readTree(p);
            String drawable = node.get("src").asText();
            String gravity = node.get("gravity").asText();
            boolean antialias = node.get("antialias").asBoolean();
            String tint = node.get("tint").asText();
            return new LayerItem(drawable, gravity, antialias, tint);
        }
    }

//    public static class Deserializer extends JsonDeserializer<List<LayerItem>> {
//
//        @Override
//        public List<LayerItem> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
//            List<LayerItem> result = new ArrayList<>();
//            ObjectCodec codec = jp.getCodec();
//            JsonNode node = codec.readTree(jp);
//
//            ObjectMapper mapper = new ObjectMapper();
//            for (JsonNode n : node) {
//                result.add(mapper.treeToValue(n, LayerItem.class));
//            }
//            return result;
//        }
//    }

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();

//        SimpleModule mod = new SimpleModule();
//        mod.addDeserializer(LayerItem.class, new Deserializer());
//        mapper.registerModule(mod);
//
//        LayerItem itemOut = null;
//        File file = new File("test.json");
//        try {
//            itemOut = mapper.readValue(file, LayerItem.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("Object read out from json:");
//        for (LayerItem item : itemOut) {
//            System.out.println(item);
//            System.out.println();
//        }

        File file = new File("test.json");
        System.out.println("File.toString = " + file.toURI());
        List<LayerItem> items = null;
        try {
            final JsonNode response = mapper.readTree(file).path("layerlist");
            final CollectionType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, LayerItem.class);
            items = mapper.reader(collectionType).readValue(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Object read from json:\n" + items);
    }
}
