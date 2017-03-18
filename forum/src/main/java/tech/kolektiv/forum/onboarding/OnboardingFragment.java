package tech.kolektiv.forum.onboarding;

import android.graphics.Color;
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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tech.kolektiv.forum.ForumApplication;
import tech.kolektiv.forum.GameActivity;
import tech.kolektiv.forum.R;
import tech.kolektiv.hiddengame.utils.ConnectorsProvider;
import tech.kolektiv.hiddengame.utils.GraphicUtils;

import static tech.kolektiv.hiddengame.utils.GraphicUtils.tintMyDrawable;

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

    @OnClick(R.id.ready)
    public void checkConnectivityAndGo(View v) {
        connectorsProvider.checkConnectivity(getActivity(), GameActivity.class);
    }

    int[] bgs = new int[]{R.drawable.onboarding1_full, R.drawable.onboarding2_full, R.drawable.onboarding3_full, R.drawable.onboarding4_full};

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
        ((ForumApplication) getActivity().getApplication()).component().inject(this);
        ButterKnife.bind(this, rootView);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
            mNextBtn.setImageDrawable(
                    tintMyDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_chevron_right_24dp), Color.WHITE)
            );

        img.setBackgroundResource(bgs[getArguments().getInt(ARG_SECTION_NUMBER) - 1]);

        mReadyButton.setVisibility(getArguments().getInt(ARG_SECTION_NUMBER) - 1 == 3 ? View.VISIBLE : View.GONE);

        return rootView;
    }


}