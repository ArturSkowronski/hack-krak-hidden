package tech.kolektiv.sdm.onboarding;

import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.facebook.share.model.ShareLinkContent;
import com.google.firebase.analytics.FirebaseAnalytics;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tech.kolektiv.hiddengame.utils.ConnectorsProvider;
import tech.kolektiv.sdm.GameActivity;
import tech.kolektiv.sdm.R;
import tech.kolektiv.sdm.SDMApplication;
import tech.kolektiv.hiddengame.utils.GraphicUtils;

import static tech.kolektiv.hiddengame.utils.FirebaseUtils.createBundle;
import static tech.kolektiv.hiddengame.utils.FirebaseUtils.createCustomBundle;

/**
 * A placeholder fragment containing a simple view.
 */
public class OnboardingFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    @Inject
    ConnectorsProvider connectorsProvider;

    private static final String ARG_SECTION_NUMBER = "section_number";

    @BindView(R.id.section_img)
    FrameLayout img;

    @BindView(R.id.intro_btn_next)
    ImageButton mNextBtn;

    @BindView(R.id.ready)
    Button mReadyButton;
    private FirebaseAnalytics mFirebaseAnalytics;

    @OnClick(R.id.ready)
    public void checkConnectivityAndGo(View v){
        mFirebaseAnalytics.logEvent("WENT_TO_GAME_FROM_ONBOARDING", createCustomBundle(true));

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, createBundle("WENT_TO_GAME_FROM_ONBOARDING"));

        connectorsProvider.checkConnectivity(getActivity(), GameActivity.class);
    }

    int[] bgs = new int[]{R.drawable.onboarding1, R.drawable.onboarding2, R.drawable.onboarding3, R.drawable.onboarding4};

    public OnboardingFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static OnboardingFragment newInstance(int sectionNumber) {
        OnboardingFragment fragment = new OnboardingFragment();
        Bundle args = new Bundle();

        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_onboarding, container, false);
        ((SDMApplication) getActivity().getApplication()).component().inject(this);
        ButterKnife.bind(this, rootView);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
            mNextBtn.setImageDrawable(
                    GraphicUtils.tintMyDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_chevron_right_24dp), Color.WHITE)
            );

        img.setBackgroundResource(bgs[getArguments().getInt(ARG_SECTION_NUMBER) - 1]);

        mReadyButton.setVisibility(getArguments().getInt(ARG_SECTION_NUMBER) - 1 == 3 ? View.VISIBLE : View.GONE);

        return rootView;
    }


}