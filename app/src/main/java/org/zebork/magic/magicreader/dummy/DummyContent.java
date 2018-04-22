package org.zebork.magic.magicreader.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.zebork.magic.magicreader.dummy.InfoGetter;
/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 4;

    static {
        // Add some sample items.
//        for (int i = 1; i <= COUNT; i++) {
//            addItem(createDummyItem(i));
//        }
        InfoGetter infoGetter = new InfoGetter();
        String sysInfo = infoGetter.getSystemInfo();
        DummyItem system = new DummyItem("1", "System Info", sysInfo);
        addItem(system);
        String cpuInfo = infoGetter.getCpuInfo();
        DummyItem cpu = new DummyItem("2", "CPU Info", cpuInfo);
        addItem(cpu);

        String memoryInfo = "Before Init";
        DummyItem mem = new DummyItem("3", "Memory Info", memoryInfo);
        addItem(mem);

        String appInfo = "";
        DummyItem app = new DummyItem("4", "APP Info", appInfo);
        addItem(app);

        String contactInfo = "";
        DummyItem contact = new DummyItem("5", "Contact Info", contactInfo);
        addItem(contact);

        String smsInfo = "";
        DummyItem sms = new DummyItem("6", "SMS Info", smsInfo);
        addItem(sms);
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

//    public static DummyItem getItem(String id) {
//        return ITEM_MAP.get(id);
//    }

    private static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public String details;
        private String dynamic = "";

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        public void setDynamic(String dynamic) {
            this.dynamic = dynamic;
        }

        public String getDynamic() {
            return dynamic;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
