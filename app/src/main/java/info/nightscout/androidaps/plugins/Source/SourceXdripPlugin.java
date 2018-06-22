package info.nightscout.androidaps.plugins.Source;

import android.os.Bundle;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import info.nightscout.androidaps.MainApp;
import info.nightscout.androidaps.R;
import info.nightscout.androidaps.Services.Intents;
import info.nightscout.androidaps.db.BgReading;
import info.nightscout.androidaps.interfaces.BgSourceInterface;
import info.nightscout.androidaps.interfaces.PluginBase;
import info.nightscout.androidaps.interfaces.PluginDescription;
import info.nightscout.androidaps.interfaces.PluginType;

/**
 * Created by mike on 05.08.2016.
 */
public class SourceXdripPlugin extends PluginBase implements BgSourceInterface {

    private static SourceXdripPlugin plugin = null;
    
    public static SourceXdripPlugin getPlugin() {
        if (plugin == null)
            plugin = new SourceXdripPlugin();
        return plugin;
    }

    private SourceXdripPlugin() {
        super(new PluginDescription()
                .mainType(PluginType.BGSOURCE)
                .fragmentClass(BGSourceFragment.class.getName())
                .pluginName(R.string.xdrip)
                .description(R.string.description_source_xdrip)
        );
    }

    @Override
    public List<BgReading> processNewData(Bundle bundle) {
        BgReading bgReading = new BgReading();

        bgReading.value = bundle.getDouble(Intents.EXTRA_BG_ESTIMATE);
        bgReading.direction = bundle.getString(Intents.EXTRA_BG_SLOPE_NAME);
        bgReading.date = bundle.getLong(Intents.EXTRA_TIMESTAMP);
        bgReading.raw = bundle.getDouble(Intents.EXTRA_RAW);
        bgReading.sourcePlugin = SourceXdripPlugin.getPlugin().getName();
        bgReading.filtered = Objects.equals(bundle.getString(Intents.XDRIP_DATA_SOURCE_DESCRIPTION), "G5 Native");

        boolean isNew = MainApp.getDbHelper().createIfNotExists(bgReading, getName());
        return isNew ? Lists.newArrayList(bgReading) : Collections.emptyList();
    }
}
