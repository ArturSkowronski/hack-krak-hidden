package tech.kolektiv.sdm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import tech.kolektiv.hiddengame.audio.AudioCallback;
import tech.kolektiv.hiddengame.audio.AudioHandler;
import tech.kolektiv.sdm.game.Scenes;
import tech.kolektiv.sdm.map.DialogMapFragment;
import tech.kolektiv.sdm.view.AnimatedCircle;
import tech.kolektiv.sdm.view.PlayerVMPresenter;
import tech.kolektiv.sdm.view.SoundVMProvider;
import tech.kolektiv.hiddengame.game.Events;

import static tech.kolektiv.hiddengame.utils.FirebaseUtils.createBundle;
import static tech.kolektiv.hiddengame.utils.FirebaseUtils.createCustomBundle;


@SuppressWarnings("ResourceType")
public class GameFragment extends Fragment {

    private int i = 0;
    public static final String PREFS_NAME = "MyPrefsFile";

    private boolean gameStarted = false;
    private boolean blockRestart = true;
    private boolean canControlSound = true;
    private int globalInstruction = 0;
    boolean isStopped = true;
    private Handler handler = new Handler();
    private int clicksToReset = 0;

    @BindView(R.id.custom_circle)
    AnimatedCircle circle;

    @BindView(R.id.circleText)
    TextView circleText;


    @BindView(R.id.bottomText)
    ImageView bottomText;

    @BindView(R.id.soundImage)
    ImageView soundImage;

    @BindView(R.id.placeNext)
    ImageView placeNext;

    @BindView(R.id.placePrev)
    ImageView placePrev;

    @BindView(R.id.mapButton)
    ImageView mapView;

    DialogMapFragment dialog = new DialogMapFragment();
    String currentState;
    @OnClick(R.id.mapButton)
    public void openMap() {
        if(dialog.isAdded())
        {
            return; //or return false/true, based on where you are calling from
        }
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, createBundle("MAP_OPENED"));
        mFirebaseAnalytics.logEvent("MAP_OPENED", createCustomBundle(true));

        dialog.show(getActivity().getSupportFragmentManager(), "Historico");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.custom_circle)
    public void customCircleOnClick() {
        if (!gameStarted) {
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, createBundle("GAME_STARTED"));
            mFirebaseAnalytics.logEvent("GAME_STARTED", createCustomBundle(true));


            sdmGame.start(currentState);
            gameStarted = true;
        }
        if (blockRestart) {
            if (isStopped) {
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, createBundle("GAME_UNPAUSED"));
                mFirebaseAnalytics.logEvent("GAME_UNPAUSED", createCustomBundle(true));

                audioInterface.play();
                isStopped = false;

                soundImage.setImageDrawable(new IconDrawable(getActivity(), MaterialIcons.md_pause).color(Color.WHITE));
            } else {
                audioInterface.pause();
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, createBundle("GAME_PAUSED"));
                mFirebaseAnalytics.logEvent("GAME_PAUSED", createCustomBundle(true));

                isStopped = true;
                soundImage.setImageDrawable(new IconDrawable(getActivity(), MaterialIcons.md_play_arrow).color(Color.WHITE));

            }
        }
        if (!blockRestart && globalInstruction != 0) {
            blockRestart = true;
            audioInterface.start(getContext(), globalInstruction);
        }
    }

    @OnClick(R.id.bottomText)
    public void bottomTextOnClick() {
        clicksToReset++;
        if (clicksToReset == 5) {
            Toast.makeText(getActivity(), "Jesteś teraz developerem!", Toast.LENGTH_LONG).show();
            placeNext.setVisibility(View.VISIBLE);
            placeNext.setImageDrawable(new IconDrawable(getActivity(), MaterialIcons.md_arrow_forward)
                    .actionBarSize());
            placePrev.setVisibility(View.VISIBLE);
            placePrev.setImageDrawable(new IconDrawable(getActivity(), MaterialIcons.md_arrow_back)
                    .actionBarSize());
            clicksToReset = 0;
        }
    }

    @OnClick(R.id.soundNext)
    public void onSNext() {
        if (canControlSound) {
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, createBundle("PLAYER_REWINDED"));
            mFirebaseAnalytics.logEvent("PLAYER_REWINDED", createCustomBundle(true));

            audioInterface.seekTo(-10000);
        }
    }

    @OnClick(R.id.soundPrev)
    public void onSPrev() {
        if (canControlSound) {
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, createBundle("PLAYER_FORWARDED"));
            mFirebaseAnalytics.logEvent("PLAYER_FORWARDED", createCustomBundle(true));
            audioInterface.seekTo(10000);
        }
    }

    @OnClick(R.id.placeNext)
    public void onNext() {
        sdmGame.trigger(++i);
    }

    @OnClick(R.id.placePrev)
    public void onPrev() {
        sdmGame.trigger(--i);
    }


    private FirebaseAnalytics mFirebaseAnalytics;

    @Inject
    SDMGame sdmGame;

    @Inject
    PlayerVMPresenter presenter;

    @Inject
    SoundVMProvider provider;

    @Inject
    AudioHandler audioInterface;

    SharedPreferences settings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        ((SDMApplication) getActivity().getApplication()).component().inject(this);
        onRestoreInstanceState(savedInstanceState);
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        ButterKnife.bind(this,
                rootView);

        settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        currentState = settings.getString("currentState", Scenes.SDM_1);


        initializeAudioInterface(circle);
        dialog.setMarker(new LatLng(50.054920, 19.938164), "Stacja 1");
        mapView.setBackground(
            new IconDrawable(getActivity(), MaterialIcons.md_map)
                .color(Color.WHITE)
        );

        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    audioInterface.pause();
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, createBundle("TELEFON_CALLED"));
                    mFirebaseAnalytics.logEvent("TELEFON_CALLED", createCustomBundle(true));

                } else if(state == TelephonyManager.CALL_STATE_IDLE) {
                    audioInterface.play();
                } else if(state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    audioInterface.pause();
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };

        TelephonyManager mgr = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);

        if(mgr != null) {
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }

        sdmGame.initializeGame();
        sdmGame.onEnd(() -> {});

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                clicksToReset = 0;
                handler.postDelayed(this, 1500);
            }
        }, 1000);

        provider.sounds()
                .subscribe(soundVM -> {
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("currentState", soundVM.getState());

                    // Commit the edits!
                    editor.apply();
                    int story = soundVM.getStory();
                    int instruction = soundVM.getInstruction();
                    String event = soundVM.getEvent();
                    if (story != 0) {
                        blockRestart = true;
                        audioInterface.start(getContext(), story);
                        if (Events.END.equals(event)) {
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, createBundle("GAME_ENDED!"));
                            mFirebaseAnalytics.logEvent("GAME_ENDED!", createCustomBundle(true));

                            audioInterface.setOnEndAction(() -> {
                                circle.setProgress(0);
                                circle.stopAnimation();
                                startActivity(new Intent(getContext(), ShareActivity.class));
                            });
                        } else {
                            if (instruction != 0) {
                                audioInterface.setOnEndAction(() -> {
                                    globalInstruction = instruction;
                                    audioInterface.start(getContext(), instruction);
                                    audioInterface.setOnEndAction(null);
                                });
                            } else {
                                globalInstruction = 0;
                                audioInterface.setOnEndAction(null);
                            }
                        }
                    } else {
                        audioInterface.stop();
                    }
                });

        presenter.views()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(playerVM -> {

                    try {
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, createBundle(playerVM.getHeaderText()));
                        mFirebaseAnalytics.logEvent("CURRENT_USER_PLACE!", createCustomBundle(playerVM.getHeaderText()));
                        mFirebaseAnalytics.logEvent(playerVM.getHeaderText(), createCustomBundle(true));
                        circleText.setText("");

                    } catch (Exception e){

                    }

                    circleText.setText("");
                    if(playerVM.getLatLng() != null) dialog.setMarker(playerVM.getLatLng(), playerVM.getHeaderText());
                    soundImage.setVisibility(View.VISIBLE);

                });

        return rootView;
    }

    public void initializeAudioInterface(final AnimatedCircle circle) {

        audioInterface.setCallback(new AudioCallback() {
            @Override
            public void onStart(long duration) {
                canControlSound = true;
                soundImage.setImageDrawable(new IconDrawable(getActivity(), MaterialIcons.md_pause).color(Color.WHITE));
                circleText.setText("");
                soundImage.setVisibility(View.VISIBLE);
                circle.setProgress(0);
                circle.stopAnimation();
                circle.animateTo(100, duration);
            }

            @Override
            public void onProgress(long position, long positionSound, long duration) {
                circle.setProgress(position);
            }

            @Override
            public void onBufferChange(int percent) {

            }

            @Override
            public void onFinished(Action0 onEnd) {
                canControlSound = false;
                circle.setProgress(0);
                circle.stopAnimation();
                audioInterface.stop();
                if (onEnd != null) {
                    onEnd.call();
                } else {
                    blockRestart = false;
                    if (globalInstruction != 0) {
                        circleText.setText(R.string.repeat_instruction);
                        soundImage.setVisibility(View.INVISIBLE);
                        audioInterface.stop();
                    } else {
                        circleText.setText(R.string.go_to_next);
                        soundImage.setVisibility(View.INVISIBLE);
                        audioInterface.stop();
                    }
                }
            }
        });
    }

    private void onRestoreInstanceState(Bundle savedInstanceState){
        if(savedInstanceState!=null){
            currentState = savedInstanceState.getString("currentState");
        }
    }

    //Here you Save your data
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentState", currentState);
    }

    @Override
    public void onActivityCreated(Bundle outState) {
        super.onActivityCreated(outState);
        onRestoreInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        audioInterface.stop();
        sdmGame.onDestroy();
        super.onDestroy();
    }
}
