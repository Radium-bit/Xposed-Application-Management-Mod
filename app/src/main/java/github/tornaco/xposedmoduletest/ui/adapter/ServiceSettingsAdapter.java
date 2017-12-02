package github.tornaco.xposedmoduletest.ui.adapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Switch;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import github.tornaco.xposedmoduletest.R;
import github.tornaco.xposedmoduletest.model.ServiceInfoSettings;
import github.tornaco.xposedmoduletest.util.ComponentUtil;
import github.tornaco.xposedmoduletest.xposed.app.XAshmanManager;

/**
 * Created by guohao4 on 2017/11/17.
 * Email: Tornaco@163.com
 */

public class ServiceSettingsAdapter extends ComponentListAdapter<ServiceInfoSettings>
        implements FastScrollRecyclerView.SectionedAdapter {

    public ServiceSettingsAdapter(Context context) {
        super(context);
    }

    private final XAshmanManager xAshmanManager = XAshmanManager.singleInstance();

    @Override
    public void onBindViewHolder(ComponentHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        final ServiceInfoSettings serviceInfoSettings = getData().get(position);

        holder.getTitleView().setText(serviceInfoSettings.simpleName());

        String processName = serviceInfoSettings.getServiceInfo().processName;
        String serviceLabel = serviceInfoSettings.getServiceLabel();


        holder.getSummaryView().setText(getContext().getString(R.string.summary_service_info_process,
                processName));
        holder.getSummaryView2().setText(getContext().getString(R.string.summary_service_info_comp,
                serviceLabel));

        if (!xAshmanManager.isServiceAvailable()) {
            return;
        }

        holder.getCompSwitch().setChecked(serviceInfoSettings.isAllowed());

        holder.getCompSwitch().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentName componentName = ComponentUtil.getComponentName(serviceInfoSettings.getServiceInfo());
                Switch s = (Switch) v;
                boolean checked = s.isChecked();

                serviceInfoSettings.setAllowed(checked);

                xAshmanManager.setComponentEnabledSetting(componentName,
                        checked ?
                                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                                : PackageManager.COMPONENT_ENABLED_STATE_DISABLED, 0);
            }
        });
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        ServiceInfoSettings infoSettings = getData().get(position);
        String name = infoSettings.simpleName();
        if (name == null || name.length() < 1) {
            name = infoSettings.toString();
        }
        return String.valueOf(name.charAt(0));
    }
}
