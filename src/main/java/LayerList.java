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
import java.util.ArrayList;
import java.util.List;

public class LayerList {
    private final String mId;
    private final List<ItemWrapper> mItemList;

    public LayerList(final String id, final List<ItemWrapper> list) {
        mId = id;
        mItemList = list;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("id:").append(mId);
        sb.append("\nlayer-list:");
        for (ItemWrapper item : mItemList) {
            sb.append("\n"+item.toString());
        }
        return sb.toString();
    }

    public static class Deserializer extends StdDeserializer<LayerList> {

        public Deserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public LayerList deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule mod = new SimpleModule();
            mod.addDeserializer(ItemWrapper.class, new ItemWrapper.Deserializer(ItemWrapper.class));
            mapper.registerModule(mod);

            ObjectCodec codec = p.getCodec();
            JsonNode node = codec.readTree(p);

            String id = node.get("id").asText();

            List<ItemWrapper> wrappers = new ArrayList<>();
            for (JsonNode n : node.get("layerlist")) {
//                System.out.println("Parsing " + n.toString());
                wrappers.add(mapper.readValue(n.toString(), ItemWrapper.class));
            }
            return new LayerList(id, wrappers);
        }
    }

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule mod = new SimpleModule();
        mod.addDeserializer(LayerList.class, new Deserializer(LayerList.class));
        mapper.registerModule(mod);

        LayerList layerList = null;
        File file = new File("test.json");
        try {
            layerList = mapper.readValue(file, LayerList.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("From JSON to java:");
        System.out.println(layerList);
    }
}
