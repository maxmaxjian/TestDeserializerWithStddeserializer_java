import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.util.Objects;

public class ItemWrapper {
    private final Item mItem;

    public ItemWrapper(final Item item) {
        Objects.nonNull(item);
        mItem = item;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("item:\n").append(mItem.toString());
        return sb.toString();
    }

    public static class Deserializer extends StdDeserializer<ItemWrapper> {

        public Deserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public ItemWrapper deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule mod = new SimpleModule();
            mod.addDeserializer(Item.class, new Item.Deserializer(Item.class));
            mapper.registerModule(mod);

            ObjectCodec codec = p.getCodec();
            JsonNode nodes = codec.readTree(p);
//            System.out.println("nodes = " + nodes);
            JsonNode content = nodes.get("item");
//            System.out.println("content = " + content);


//            Item item = null;
//            for (JsonNode n : nodes) {
//                item = mapper.readValue(n.toString(), Item.class);
//            }
            Item item = mapper.readValue(content.toString(), Item.class);
            return new ItemWrapper(item);
        }
    }
}
