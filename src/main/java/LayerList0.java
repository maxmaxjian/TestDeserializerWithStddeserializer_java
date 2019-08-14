import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.DataInput;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LayerList0 {
    private final List<LayerItem> mLayerList;

    public LayerList0(final List<LayerItem> layerList) {
        mLayerList = layerList;
    }

    public List<LayerItem> getList() {
        return mLayerList;
    }

//    public static class Item {
//        private final LayerItem mLayerItem;
//
//        public Item(final LayerItem layerItem) {
//            mLayerItem = layerItem;
//        }
//
//        public LayerItem getLayerItem() {
//            return mLayerItem;
//        }
//    }

//    public static class Deserializer extends JsonDeserializer<List<LayerItem>> {
//        private final ObjectMapper mapper = new ObjectMapper();
//        private final CollectionType collectionType =
//                TypeFactory
//                .defaultInstance()
//                .constructCollectionType(List.class, LayerItem.class);
//
//        @Override
//        public List<LayerItem> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
//            ObjectNode node = mapper.readTree(jsonParser);
//            JsonNode nodeList = node.get("layerlist");
//            if (null == nodeList || !nodeList.isArray() || !nodeList.elements().hasNext()) {
//                return null;
//            }
//            return mapper.reader(collectionType).readValue(nodeList);
//        }
//    }

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule mod = new SimpleModule();
        mod.addDeserializer(LayerItem.class, new LayerItem.Deserializer(LayerItem.class));
        mapper.registerModule(mod);

//        final CollectionType collectionType =
//                TypeFactory
//                        .defaultInstance()
//                        .constructCollectionType(List.class, LayerItem.class);

        List<LayerItem> items = new ArrayList<>();
        try {
            JsonNode node = mapper.readTree(new File("test.json")).path("layerlist");
            System.out.println("node = " + node);
            for (JsonNode n : node) {
                System.out.println(n);
                items.add(mapper.readValue(n.toString(), LayerItem.class));
            }
            JsonNode nodeList = node.get("item");
            System.out.println("nodeList = " + nodeList);
//            items = mapper.reader(collectionType).readValue(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(items);
    }
}
