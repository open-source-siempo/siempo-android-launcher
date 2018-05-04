package co.siempo.phone.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import co.siempo.phone.R;
import co.siempo.phone.app.CoreApplication;
import co.siempo.phone.helper.ActivityHelper;
import co.siempo.phone.helper.FirebaseHelper;
import co.siempo.phone.utils.PrefSiempo;

import static co.siempo.phone.activities.DashboardActivity.IS_FROM_HOME;

/**
 * Created by hardik on 17/8/17.
 */


@EActivity(R.layout.activity_siempo_alpha_settings)
public class AlphaSettingsActivity extends CoreActivity {

    @ViewById
    ImageView icon_UserId;
    @ViewById
    TextView txt_UserId;
    private Context context;
    private long startTime = 0;
    private LinearLayout ln_suppressedNotifications;
    private RelativeLayout rel_restrictions;
    private Switch switch_alphaRestriction;
    private ImageView icon_SuppressedNotifications;
    private LinearLayout ln_permissions;
    private ImageView icon_permissions, icon_in_app;
    private Toolbar toolbar;
    private LinearLayout linInAppProduct;

    @AfterViews
    void afterViews() {
        initView();
        onClickEvents();
    }


    public void initView() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_blue_24dp);
        toolbar.setTitle(R.string.alpha_settings);
//        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color
//                .colorAccent));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        context = AlphaSettingsActivity.this;
        ln_suppressedNotifications = findViewById(R.id.ln_suppressedNotifications);
        rel_restrictions = findViewById(R.id.rel_restrictions);
        switch_alphaRestriction = findViewById(R.id.switch_alphaRestriction);
        ln_permissions = findViewById(R.id.ln_permissions);
        linInAppProduct = findViewById(R.id.linInAppProduct);
        icon_SuppressedNotifications = findViewById(R.id.icon_SuppressedNotifications);
        icon_permissions = findViewById(R.id.icon_permissions);
        icon_in_app = findViewById(R.id.icon_in_app);
        icon_permissions.setImageDrawable(new IconDrawable(context, "fa-bell").colorRes(R.color.text_primary).sizeDp(18));
        try {
            icon_SuppressedNotifications.setImageDrawable(new IconDrawable(context, "fa-exclamation").colorRes(R.color.text_primary).sizeDp(18));
        } catch (Exception e) {
            //Todo log exception to fabric
            e.printStackTrace();
//            Crashlytics.logException(e);
        }
        icon_UserId.setImageDrawable(new IconDrawable(context, "fa-user-secret")
                .colorRes(R.color.text_primary)
                .sizeDp(18));
        icon_in_app.setImageDrawable(new IconDrawable(context, "fa-shopping-cart")
                .colorRes(R.color.text_primary)
                .sizeDp(18));
        txt_UserId.setText(String.format("UserId: %s", CoreApplication.getInstance().getDeviceId()));
        if(PrefSiempo.getInstance(this).read(PrefSiempo.JUNK_RESTRICTED,false))
        {
            switch_alphaRestriction.setChecked(true);
        }
        else

        {
            switch_alphaRestriction.setChecked(false);
        }

    }

    public void onClickEvents() {

        ln_suppressedNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new ActivityHelper(context).openSiempoSuppressNotificationsSettings();
            }
        });

        ln_permissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlphaSettingsActivity.this, SiempoPermissionActivity_.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra(IS_FROM_HOME, false);
                startActivity(intent);
            }
        });
        rel_restrictions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (switch_alphaRestriction.isChecked()) {
                    switch_alphaRestriction.setChecked(false);
                    PrefSiempo.getInstance(context).write(PrefSiempo.JUNK_RESTRICTED,
                            false);
                } else {
                    switch_alphaRestriction.setChecked(true);
                    PrefSiempo.getInstance(context).write(PrefSiempo.JUNK_RESTRICTED,
                            true);
                }
            }
        });


        linInAppProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlphaSettingsActivity.this, InAppItemListActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        FirebaseHelper.getInstance().logScreenUsageTime(AlphaSettingsActivity.this.getClass().getSimpleName(), startTime);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTime = System.currentTimeMillis();
//        PackageUtil.checkPermission(this);
    }


}
