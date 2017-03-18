package tech.kolektiv.forum;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import tech.kolektiv.forum.game.ForumGame;
import tech.kolektiv.forum.view.AnimatedCircle;
import tech.kolektiv.forum.view.PlayerVMPresenter;
import tech.kolektiv.forum.view.SoundVMProvider;
import tech.kolektiv.hiddengame.game.Events;

import static tech.kolektiv.forum.game.Scenes.FORUM_1;

@SuppressWarnings("ResourceType")
public class GameFragment extends Fragment {

    private static final String TAG = "EDDY_BEACON_LISTENER";

    private int i = 0;

    private boolean gameStarted = false;
    private boolean blockRestart = true;
    private boolean canControlSound = true;

    @BindView(R.id.custom_circle)
    AnimatedCircle circle;

    @BindView(R.id.circleText)
    TextView circleText;

    @BindView(R.id.headingTextView)
    TextView header;

    @BindView(R.id.bottomText)
    TextView bottomText;

    @BindView(R.id.soundImage)
    ImageView soundImage;

    @BindView(R.id.placeNext)
    ImageView placeNext;

    @BindView(R.id.placePrev)
    ImageView placePrev;
    private int globalInstruction = 0;
    boolean isStopped = true;

    @OnClick(R.id.custom_circle)
    public void customCircleOnClick() {
        if (!gameStarted) {
            forumGame.start(FORUM_1.name());
            gameStarted = true;
        }
        if (blockRestart) {
            if (isStopped) {
                audioInterface.play();
                isStopped = false;
                soundImage.setImageDrawable(new IconDrawable(getActivity(), MaterialIcons.md_pause).color(Color.WHITE));
            } else {
                audioInterface.pause();
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
        if (canControlSound) audioInterface.seekTo(-10000);
    }

    @OnClick(R.id.soundPrev)
    public void onSPrev() {
        if (canControlSound) audioInterface.seekTo(10000);
    }

    @OnClick(R.id.placeNext)
    public void onNext() {
        forumGame.trigger(++i);
    }

    @OnClick(R.id.placePrev)
    public void onPrev() {
        forumGame.trigger(--i);
    }

    private Handler handler = new Handler();
    private int clicksToReset = 0;

    @Inject
    ForumGame forumGame;

    @Inject
    PlayerVMPresenter presenter;

    @Inject
    SoundVMProvider provider;

    @Inject
    AudioHandler audioInterface;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((ForumApplication) getActivity().getApplication()).component().inject(this);

        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        ButterKnife.bind(this, rootView);

        initializeAudioInterface(circle);
        forumGame.initializeGame();

        forumGame.onEnd(() -> {
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                clicksToReset = 0;
                handler.postDelayed(this, 1500);
            }
        }, 1000);

        provider.sounds()
                .subscribe(soundVM -> {
                    int story = soundVM.getStory();
                    int instruction = soundVM.getInstruction();
                    String event = soundVM.getEvent();
                    if (story != 0) {
                        blockRestart = true;
                        audioInterface.start(getContext(), story);
                        if (Events.END.equals(event)) {
                            audioInterface.setOnEndAction(() -> {
                                circle.setProgress(0);
                                circle.stopAnimation();
                                startActivity(new Intent(getContext(), WebViewActivity.class));
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
                    if (playerVM.getHeaderText() != null) header.setText(playerVM.getHeaderText());
                    circleText.setText("");
//                    bottomText.setText(playerVM.getDistance());
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
                        circleText.setText("Powtórz instrukcje");
                        soundImage.setVisibility(View.INVISIBLE);
                        audioInterface.stop();
                    } else {
                        circleText.setText("Idź do punktu");
                        soundImage.setVisibility(View.INVISIBLE);
                        audioInterface.stop();
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        audioInterface.stop();
        forumGame.onDestroy();
        super.onDestroy();
    }
}
